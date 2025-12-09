import React, { useState } from "react";
import axios from "axios";
import { useSelector } from "react-redux";

import "../../../../css/admin/container/AdminAddItemContainer.css";
import jwtAxios from "../../../../apis/util/jwtUtil";

const AdminAddItemContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const memberId = useSelector(state => state.loginSlice.id);
  const nickName = useSelector(state => state.loginSlice.nickName);

  const [itemDto, setItemDto] = useState({
    itemTitle: "",
    itemPrice: "",
    itemSize: "",
    itemDetail: "",
    category: "",
  });

  const [file, setFile] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    formData.append(
      "itemDto",
      new Blob([JSON.stringify(itemDto)], { type: "application/json" })
    );

    formData.append("itemFile", file);

    try {
      await jwtAxios.post("/api/admin/item/insert", formData, 
     {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true
        });
      alert("상품 등록 완료!");
    } catch (error) {
      console.error(error);
     
      alert("상품 등록 실패");
    }
  };

  return (
    <div className="admin-add-item">
      <h2>상품 등록</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="상품명"
          onChange={(e) =>
            setItemDto({ ...itemDto, itemTitle: e.target.value })
          }
        />

        <input
          type="number"
          placeholder="가격"
          onChange={(e) =>
            setItemDto({ ...itemDto, itemPrice: e.target.value })
          }
        />

        <textarea
          placeholder="상세 설명"
          onChange={(e) =>
            setItemDto({ ...itemDto, itemDetail: e.target.value })
          }
        />

        <input
          type="number"
          placeholder="재고"
          onChange={(e) => setItemDto({ ...itemDto, itemSize: e.target.value })}
        />

        <select
          onChange={(e) => setItemDto({ ...itemDto, category: e.target.value })}
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

        <input type="file" onChange={(e) => setFile(e.target.files[0])} />

        <button type="submit">등록하기</button>
      </form>
    </div>
  );
};

export default AdminAddItemContainer;
