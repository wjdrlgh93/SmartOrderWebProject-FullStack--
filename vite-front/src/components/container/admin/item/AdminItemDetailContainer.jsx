import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { BACK_BASIC_URL, IMAGES_S3_URL } from "../../../../apis/commonApis";

import "../../../../css/admin/container/AdminItemDetailContainer.css";
import jwtAxios from "../../../../apis/util/jwtUtil";

const AdminItemDetailContainer = () => {
  const { itemId } = useParams();
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [item, setItem] = useState({
    itemTitle: "",
    itemPrice: "",
    itemDetail: "",
    itemSize: "",
    itemImage: "", // 🔥 기존 이미지 URL 받기
    category: "",
  });

  const [file, setFile] = useState(null);

  // 상세 불러오기
  const fetchItemDetail = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/item/detail/${itemId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );
      setItem(res.data);
    } catch (err) {
      console.error(err);
      alert("상품 정보를 불러올 수 없습니다.");
    }
  };

  useEffect(() => {
    fetchItemDetail();
  }, []);

  // 수정 처리
  const handleUpdate = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append(
      "dto",
      new Blob([JSON.stringify(item)], { type: "application/json" })
    );

    if (file) {
      formData.append("itemFile", file);
    }

    try {
      await jwtAxios.put(
        `${BACK_BASIC_URL}/api/admin/item/update/${itemId}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      alert("상품 수정 완료!");
      navigate("/admin/itemList");
    } catch (err) {
      console.error(err);
      alert("상품 수정 실패");
    }
  };

  console.log("TOKEN", accessToken);

  const handleDeleteImage = async () => {
    if (!window.confirm("정말 상품 이미지를 삭제하시겠습니까?")) return;
    try {
      await jwtAxios.delete(
        `${BACK_BASIC_URL}/api/admin/item/image/delete/${itemId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );

      setItem({ ...item, attachFile: 0, itemImage: null });
      alert("이미지 삭제 완료");
    } catch (err) {
      console.error(err);
      alert("이미지 삭제 실패");
    }
  };

  return (
    <div className="admin-item-detail">
      {console.log(item)}
      {console.log("IMAGES_S3_URL:", IMAGES_S3_URL)}
      {console.log("item:", item)}
      {item.itemImgDtos && item.itemImgDtos.length > 0 &&
        console.log("newFileName:", item.itemImgDtos[0].newName)}
      <h2>상품 상세 / 수정</h2>
      <div className="item-detail-con">
        <div className="detail-left">
          {/* 🔥 기존 이미지 미리보기 */}
          <div className="detail-img">
            {item.itemImgDtos && item.itemImgDtos.length > 0 && (
              <img
                src={`${IMAGES_S3_URL}/${item.itemImgDtos[0].newName}`}
                alt="상품 이미지"
                width="250"
                height="350"
              />
            )}
          </div>
          <button type="button" onClick={handleDeleteImage}>
            이미지 삭제
          </button>
        </div>
        <div className="detail-right">
          <form onSubmit={handleUpdate} encType="multipart/form-data">
            <label>상품명</label>
            <input
              type="text"
              value={item.itemTitle}
              onChange={(e) => setItem({ ...item, itemTitle: e.target.value })}
            />

            <label>가격</label>
            <input
              type="number"
              value={item.itemPrice}
              onChange={(e) => setItem({ ...item, itemPrice: e.target.value })}
            />

            <label>상세 설명</label>
            <textarea
              value={item.itemDetail}
              onChange={(e) => setItem({ ...item, itemDetail: e.target.value })}
            />

            <label>재고</label>
            <input
              type="number"
              value={item.itemSize}
              onChange={(e) => setItem({ ...item, itemSize: e.target.value })}
            />

            <select
              value={item.category}
              onChange={(e) => setItem({ ...item, category: e.target.value })}
            >
              <option value="">카테고리 선택 (필수)</option>
              <option value="ACCESSORY">악세사리</option>
              <option value="CLOTHES">의류</option>
              <option value="EQUIPMENT">장비류</option>
              <option value="NUTRITION">영양보조식품</option>
              <option value="SHOES">신발</option>
              <option value="SALES">세일품목</option>
              <option value="ETC">기타</option>
            </select>

            <label>상품 이미지 변경</label>
            <input
              type="file"
              onChange={(e) => {
                setFile(e.target.files[0]);
                setItem({ ...item, attachFile: 1 });
              }}
            />

            <button type="submit">수정하기</button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AdminItemDetailContainer;
