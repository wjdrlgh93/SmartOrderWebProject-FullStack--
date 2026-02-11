import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import "../../../../css/admin/container/AdminItemListContainer.css";
import jwtAxios from "../../../../apis/util/jwtUtil";

const AdminItemListContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const keyword = params.get("keyword") || "";

  const navigate = useNavigate();
  const [items, setItems] = useState([]);

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [searchText, setSearchText] = useState("");

  // 검색 기능
  const handleSearch = () => {
    if (!searchText.trim()) {
      alert("검색어를 입력하세요");
      return;
    }
    navigate(`/admin/itemList?keyword=${searchText}`);
  };

  useEffect(() => {
    fetchItems(keyword);
  }, [keyword]);

  useEffect(() => {
    fetchItems();
  }, [page]);

  //  목록 가져오기
  const fetchItems = async (keyword) => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/item/itemList`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          params: { page: page, size: 10, keyword: keyword },

          withCredentials: true,
        }
      );

      setItems(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error(err);
      alert("상품 목록 불러오기 실패");
    }
  };

  // 수정 이동
  const handleUpdate = (itemId) => {
    navigate(`/admin/itemDetail/${itemId}`);
  };

  //  삭제
  const handleDelete = async (itemId) => {
    if (!window.confirm("정말 이 상품을 삭제하시겠습니까?")) return;

    try {
      await jwtAxios.delete(`/api/admin/item/delete/${itemId}`);
      alert("상품이 삭제되었습니다.");
      fetchItems();
    } catch (err) {
      console.error(err);
      alert("삭제 실패");
    }
  };

  return (
    <div className="admin-itemList">
      <div className="admin-itemList-con">

        <div className="admin-itemList-header">
          <h2>상품 목록</h2>

          <div className="admin-itemList-search">
            <input
              type="text"
              placeholder="상품명 검색"
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
            />
            <button onClick={handleSearch}>검색</button>
          </div>
        </div>

        <div className="admin-itemList-table-wrapper">
          <table className="admin-itemList-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>상품명</th>
                <th>가격</th>
                <th>수정</th>
                <th>삭제</th>
              </tr>
            </thead>

            <tbody>
              {items.length > 0 ? (
                items.map((item) => (
                  <tr key={item.id}>
                    <td>{item.id}</td>
                    <td>{item.itemTitle}</td>
                    <td>{item.itemPrice}</td>

                    <td>
                      <button className="update-btn" onClick={() => handleUpdate(item.id)}>
                        수정
                      </button>
                    </td>

                    <td>
                      <button className="delete-btn" onClick={() => handleDelete(item.id)}>
                        삭제
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5">관리자만 열람할 수 있는 페이지 입니다.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

        <div className="pagination">
          {[...Array(totalPages)].map((_, i) => (
            <button
              key={i}
              onClick={() => setPage(i)}
              className={i === page ? "active" : ""}
            >
              {i + 1}
            </button>
          ))}
        </div>

      </div>
    </div>
  );
};

export default AdminItemListContainer;
