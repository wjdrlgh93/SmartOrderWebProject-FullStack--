import React, { useEffect, useState, useMemo } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getCartByToken,
  addItemToCart,
  removeCartItem,
  getCartItemsByPage,
  updateCartItemQuantity,
} from "../../../apis/cart/cartApi";
import "../../../css/cart/CartPage.css";

const BASE_IMAGE_URL =
  "https://spring-project-test.s3.ap-northeast-2.amazonaws.com/";
const NO_IMAGE_URL = "/images/noimage.jpg";

export default function CartPage() {
  const navigate = useNavigate();
  const location = useLocation();
  // 상품 상세에서 장바구니에 추가하기 위해 넘겨받은 아이템 정보
  const itemToAdd = location.state?.itemToAdd;

  const [cart, setCart] = useState(null);
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({});
  const [keyword, setKeyword] = useState("");
  const pageSize = 5;

  // 체크된 아이템 ID (cartItemId) 목록을 저장
  const [checkedItems, setCheckedItems] = useState(new Set());
  // 체크된 아이템의 가격과 수량을 저장하여 총합계를 계산하는 데 사용
  const [selectedItemsData, setSelectedItemsData] = useState(new Map());

  // 장바구니 정보(CartId)를 불러오는 함수
  const fetchCart = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("로그인이 필요합니다.");

      const data = await getCartByToken(token);
      setCart(data);
    } catch (e) {
      console.error("장바구니 불러오기 실패:", e);
      alert("로그인이 필요합니다.");
      navigate("/auth/login", { state: { from: location, itemToAdd } });
    } finally {
      setLoading(false);
    }
  };

  // 장바구니 아이템 목록을 페이지네이션하여 불러오는 함수
  const fetchItems = async () => {
    if (!cart) return;
    setLoading(true);
    try {
      const data = await getCartItemsByPage(
        cart.cartId,
        currentPage,
        pageSize,
        keyword
      );

      const fetchedItems = data.content || [];
      setItems(fetchedItems);

      console.log("Fetched Cart Items:", fetchedItems);

      setPageInfo({
        totalPages: data.totalPages,
        hasNext: data.hasNext,
        hasPrevious: data.hasPrevious,
      });

      // 새로운 목록을 불러왔을 때, 이전 페이지에서 체크했던 아이템이 현재 페이지에 없다면
      // checkedItems와 selectedItemsData에서 제거하여 동기화
      const newCheckedItems = new Set();
      const newSelectedItemsData = new Map();

      fetchedItems.forEach((item) => {
        if (checkedItems.has(item.cartItemId)) {
          newCheckedItems.add(item.cartItemId);
          // 아이템 정보도 현재 불러온 최신 정보로 갱신
          newSelectedItemsData.set(item.cartItemId, {
            price: item.itemPrice,
            quantity: item.itemSize || 1,
          });
        }
      });
      setCheckedItems(newCheckedItems);
      setSelectedItemsData(newSelectedItemsData);
    } catch (e) {
      console.error("장바구니 아이템 조회 실패:", e);
    } finally {
      setLoading(false);
    }
  };

  // 1. 컴포넌트 마운트 시 장바구니 정보를 불러옴
  useEffect(() => {
    fetchCart();
  }, []);

  // 2. 장바구니 정보, 페이지, 검색어가 변경되면 아이템 목록을 다시 불러옴
  useEffect(() => {
    fetchItems();
  }, [cart, currentPage, keyword]);

  // 3. 상품 상세 페이지에서 넘어온 itemToAdd가 있으면 장바구니에 추가
  useEffect(() => {
    if (!cart || !itemToAdd) return;

    const addItem = async () => {
      try {
        // itemToAdd에 quantity가 없으면 기본값 1
        const quantity = itemToAdd.quantity || 1;
        await addItemToCart(cart.cartId, itemToAdd.id, quantity);
        fetchItems(); // 추가 후 목록 갱신
        // itemToAdd 정보를 제거하여, 페이지를 리로드해도 다시 추가되지 않도록 함
        navigate("/cart", { replace: true });
      } catch (e) {
        console.error("상품 추가 실패:", e);
      }
    };
    addItem();
  }, [cart, itemToAdd, navigate]);

  // 장바구니 아이템 삭제 핸들러
  const handleRemoveItem = async (cartItemId) => {
    try {
      await removeCartItem(cartItemId);

      // 삭제된 아이템 ID를 체크 목록과 선택된 데이터 맵에서 제거
      setCheckedItems((prev) => {
        const newSet = new Set(prev);
        newSet.delete(cartItemId);
        return newSet;
      });

      setSelectedItemsData((prev) => {
        const newMap = new Map(prev);
        newMap.delete(cartItemId);
        return newMap;
      });

      fetchItems(); // 목록 갱신
    } catch (e) {
      console.error("삭제 실패:", e);
    }
  };

  // 검색 버튼 클릭 핸들러: 페이지를 0으로 초기화하고 검색 수행 (useEffect가 keyword와 currentPage 변화를 감지하여 fetchItems 실행)
  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0);
  };

  // 페이지네이션 핸들러
  const handlePrev = () =>
    pageInfo.hasPrevious && setCurrentPage((prev) => prev - 1);
  const handleNext = () =>
    pageInfo.hasNext && setCurrentPage((prev) => prev + 1);

  // 개별 아이템 체크박스 핸들러
  const handleCheckItem = (cartItemId, isChecked) => {
    const itemData = items.find((item) => item.cartItemId === cartItemId);
    if (!itemData) return;

    setCheckedItems((prevChecked) => {
      const newCheckedItems = new Set(prevChecked);
      if (isChecked) {
        newCheckedItems.add(cartItemId);
      } else {
        newCheckedItems.delete(cartItemId);
      }
      return newCheckedItems;
    });

    setSelectedItemsData((prevSelected) => {
      const newSelectedItemsData = new Map(prevSelected);
      if (isChecked) {
        newSelectedItemsData.set(cartItemId, {
          price: itemData.itemPrice,
          quantity: itemData.itemSize || 1,
        });
      } else {
        newSelectedItemsData.delete(cartItemId);
      }
      return newSelectedItemsData;
    });
  };

  // 전체 아이템 체크박스 핸들러
  const handleCheckAll = (isChecked) => {
    const updatedCheckedItems = new Set();
    const newSelectedItemsData = new Map();

    if (isChecked) {
      items.forEach((item) => {
        updatedCheckedItems.add(item.cartItemId);
        newSelectedItemsData.set(item.cartItemId, {
          price: item.itemPrice,
          quantity: item.itemSize || 1,
        });
      });
    }
    // isChecked가 false이면, updatedCheckedItems와 newSelectedItemsData는 빈 상태로 남음

    setCheckedItems(updatedCheckedItems);
    setSelectedItemsData(newSelectedItemsData);
  };

  // 수량 변경 핸들러
  const handleQuantityChange = async (cartItemId, newQuantity) => {
    const quantity = Math.max(1, newQuantity); // 수량은 최소 1

    // 1. selectedItemsData (총합계 계산용 상태) 업데이트
    setSelectedItemsData((prev) => {
      if (prev.has(cartItemId)) {
        const map = new Map(prev);
        map.set(cartItemId, {
          ...map.get(cartItemId),
          quantity,
        });
        return map;
      }
      return prev; // 체크되지 않은 항목은 업데이트하지 않음
    });

    // 2. items (화면 렌더링용 상태) 업데이트
    setItems((prev) =>
      prev.map((item) =>
        item.cartItemId === cartItemId
          ? { ...item, itemSize: quantity } // itemSize는 수량 필드
          : item
      )
    );

    // 3. 서버에 수량 업데이트 요청
    try {
      await updateCartItemQuantity(cartItemId, quantity);
    } catch (e) {
      console.error("수량 업데이트 실패:", e);
      // 서버 업데이트 실패 시, UI 상태를 롤백하거나 (fetchItems) 사용자에게 알림
      fetchItems();
    }
  };

  // 총 합계 및 전체 선택 상태 계산 (useMemo)
  const { isAllChecked, checkedItemsTotal } = useMemo(() => {
    const allItemIds = items.map((item) => item.cartItemId);
    // 현재 페이지의 모든 아이템 ID가 checkedItems Set에 포함되어 있는지 확인
    const isAllChecked =
      items.length > 0 && allItemIds.every((id) => checkedItems.has(id));

    let total = 0;
    // selectedItemsData Map에 저장된 정보로 총 합계 계산
    for (const data of selectedItemsData.values()) {
      total += data.price * data.quantity;
    }
    return { isAllChecked, checkedItemsTotal: total };
  }, [items, checkedItems, selectedItemsData]);

  // 개별 아이템의 합계 계산 함수
  const getItemTotal = (item) => {
    const quantity = item.itemSize || 1;
    const price = item.itemPrice || 0;
    return price * quantity;
  };

  if (loading) return <p>로딩 중...</p>;

  return (
    <div className="cartPage">
      <h1>장바구니 목록</h1>

      <form onSubmit={handleSearch} className="cartSearch">
        <input
          type="text"
          placeholder="상품명 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button type="submit">검색</button>
      </form>

      {/* 장바구니가 비었을 때 */}
      {items.length === 0 ? (
        <div className="cart-empty-container">
          <p className="cart-empty-message">장바구니에 상품이 없습니다.</p>
        </div>
      ) : (
        <>
          <table className="cartTable">
            <thead>
              <tr>
                <th>
                  <input
                    type="checkbox"
                    checked={isAllChecked}
                    onChange={(e) => handleCheckAll(e.target.checked)}
                  />
                </th>
                <th>이미지</th>
                <th>상품명</th>
                <th>가격</th>
                <th>수량</th>
                <th>합계</th>
                <th>삭제</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => {
                // 이미지 경로 생성: 파일 이름과 기본 URL을 결합
                const imageUrl = item.itemImage
                  ? `${BASE_IMAGE_URL}${item.itemImage}`
                  : null;

                return (
                  <tr key={item.cartItemId}>
                    {console.log(item)}
                    <td>
                      <input
                        type="checkbox"
                        checked={checkedItems.has(item.cartItemId)}
                        onChange={(e) =>
                          handleCheckItem(item.cartItemId, e.target.checked)
                        }
                      />
                    </td>

                    <td>
                      {imageUrl ? (
                        <img
                          src={imageUrl}
                          alt={item.itemTitle || "상품 이미지"}
                          className="cart-item-thumbnail"
                        />
                      ) : (
                        <img
                          src={NO_IMAGE_URL}
                          alt="이미지 없음"
                          className="cart-item-thumbnail no-image"
                        />
                      )}
                    </td>

                    <td>{item.itemTitle}</td>

                    <td className="price-column">
                      {item.itemPrice.toLocaleString()}원
                    </td>

                    <td>
                      <div className="quantity-control">
                        <button
                          onClick={() =>
                            handleQuantityChange(
                              item.cartItemId,
                              (item.itemSize || 1) - 1
                            )
                          }
                          disabled={(item.itemSize || 1) <= 1}
                        >
                          -
                        </button>
                        <input
                          type="number"
                          min="1"
                          value={item.itemSize || 1}
                          onChange={(e) =>
                            handleQuantityChange(
                              item.cartItemId,
                              parseInt(e.target.value) || 1
                            )
                          }
                        />
                        <button
                          onClick={() =>
                            handleQuantityChange(
                              item.cartItemId,
                              (item.itemSize || 1) + 1
                            )
                          }
                        >
                          +
                        </button>
                      </div>
                    </td>

                    <td>{getItemTotal(item).toLocaleString()}원</td>

                    <td>
                      <button onClick={() => handleRemoveItem(item.cartItemId)}>
                        삭제
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          <div className="cartSummary">
            <p>총 상품 가격: {checkedItemsTotal.toLocaleString()}원</p>
            <p>배송비: 무료</p>
            <p className="finalTotal">
              최종 결제 금액: {checkedItemsTotal.toLocaleString()}원
            </p>
          </div>
        </>
      )}

      {/* 페이지네이션 */}
      <div className="pagination">
        <button onClick={handlePrev} disabled={!pageInfo.hasPrevious}>
          이전
        </button>
        <span>
          페이지 {currentPage + 1} / {pageInfo.totalPages || 1}
        </span>
        <button onClick={handleNext} disabled={!pageInfo.hasNext}>
          다음
        </button>
      </div>

      {/* 하단 링크 및 결제 버튼 */}
      <div className="bottomLinks">
        <button onClick={() => navigate("/store")}>상품 리스트</button>
        <button
          onClick={() =>
            navigate("/payment", {
              state: { checkedItems: Array.from(checkedItems) },
            })
          }
          disabled={checkedItems.size === 0}
        >
          결제 ({checkedItems.size}개)
        </button>
      </div>
    </div>
  );
}
