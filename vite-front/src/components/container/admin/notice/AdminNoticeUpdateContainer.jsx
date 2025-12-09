import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { Link, useParams } from "react-router";
import { useSelector } from "react-redux";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";

const AdminNoticeUpdateContainer = () => {
  const { noticeId } = useParams();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const memberId = useSelector((state) => state.loginSlice.id);

  const [noticeUpdate, setNoticeUpdate] = useState({
    title: "",
    content: "",
    memberId: memberId,
    id: noticeId,
  });

  const AdminNoticeDetailFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/board/notice/detail/${noticeId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      console.log(res.data);
      setNoticeUpdate(res.data);
    } catch (err) {
      console.log("공지글 상세조회 실패, ", err);
    }
  };

  const AdminNoticeUpdateFn = async () => {
    try {
      const res = await jwtAxios.put(
        `${BACK_BASIC_URL}/api/admin/board/notice/update`,
        noticeUpdate,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      console.log(res);
      alert("글 수정 되었습니다.");
    } catch (err) {
      console.log("공지글 상세조회 실패, ", err);
    }
  };

  useEffect(() => {
    AdminNoticeDetailFn();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNoticeUpdate((el) => ({ ...el, [name]: value }));
  };

  return (
    <div className="admin-notice-update">
      <div className="admin-notice-update__container">
        <h1 className="admin-notice-update__title">공지수정</h1>

        <div className="admin-notice-update__form">
          <div className="admin-notice-update__form-group">
            <label htmlFor="title" className="admin-notice-update__label">
              제목 <span className="admin-notice-update__required">*</span>
            </label>
            <input
              type="text"
              name="title"
              id="title"
              value={noticeUpdate.title}
              onChange={handleChange}
              placeholder="공지사항 제목을 입력하세요"
              maxLength={100}
              className="admin-notice-update__input"
            />
          </div>

          <div className="admin-notice-update__form-group">
            <label htmlFor="content" className="admin-notice-update__label">
              내용 <span className="admin-notice-update__required">*</span>
            </label>
            <textarea
              name="content"
              id="content"
              value={noticeUpdate.content}
              onChange={handleChange}
              placeholder="공지사항 내용을 입력하세요"
              rows={15}
              className="admin-notice-update__textarea"
            />
          </div>

          <div className="admin-notice-update__button-group">
            <Link to="/admin/noticeList">글목록으로</Link>
            <button
              type="button"
              className="admin-notice-update__btn admin-notice-update__btn--submit"
              onClick={AdminNoticeUpdateFn}
            >
              수정
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminNoticeUpdateContainer;
