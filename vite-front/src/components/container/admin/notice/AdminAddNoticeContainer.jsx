import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router";

const AdminAddNoticeContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const memberId = useSelector((state) => state.loginSlice.id);
  const navigate = useNavigate();
  const [addNotice, setAddNotice] = useState({
    title: "",
    content: "",
    category: "NOTICE",
    memberId: memberId,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAddNotice((el) => ({ ...el, [name]: value }));
  };

  const adminAddNoticeFn = async () => {
    try {
      const res = await jwtAxios.post(
        `${BACK_BASIC_URL}/api/admin/board/write/notice`,
        addNotice,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      alert("공지사항 등록이 완료되었습니다.");
      navigate("/admin/noticeList");
    } catch (err) {
      alert("알 수 없는 이유로 등록 실패했습니다. 다시 시도해주세요.");
      console.log("공지등록 실패, ", err);
    }
  };

  return (
    <div className="admin-notice-write">
      <div className="admin-notice-write__container">
        <h1 className="admin-notice-write__title">공지사항 등록</h1>

        <div className="admin-notice-write__form">
          <div className="admin-notice-write__form-group">
            <label htmlFor="title" className="admin-notice-write__label">
              제목 <span className="admin-notice-write__required">*</span>
            </label>
            <input
              type="text"
              name="title"
              id="title"
              onChange={handleChange}
              placeholder="공지사항 제목을 입력하세요"
              maxLength={100}
              className="admin-notice-write__input"
            />
          </div>

          <div className="admin-notice-write__form-group">
            <label htmlFor="content" className="admin-notice-write__label">
              내용 <span className="admin-notice-write__required">*</span>
            </label>
            <textarea
              name="content"
              id="content"
              onChange={handleChange}
              placeholder="공지사항 내용을 입력하세요"
              rows={15}
              className="admin-notice-write__textarea"
            />
          </div>

          <div className="admin-notice-write__button-group">
            <button
              type="button"
              onClick={adminAddNoticeFn}
              className="admin-notice-write__btn admin-notice-write__btn--submit"
            >
              등록
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminAddNoticeContainer;
