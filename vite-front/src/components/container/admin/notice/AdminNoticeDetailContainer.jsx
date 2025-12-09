import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { Link, useNavigate, useParams } from "react-router";
import { useSelector } from "react-redux";
import { formatDate } from "../../../../js/formatDate";

const AdminNoticeDetailContainer = () => {
  const { noticeId } = useParams();
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [noticeDetail, setNoticeDetail] = useState({});

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
      setNoticeDetail(res.data);
    } catch (err) {
      console.log("공지글 상세조회 실패, ", err);
    }
  };

  const AdminNoticeDeleteFn = async () => {
    try {
      const boolRes = window.confirm("이 공지를 삭제하시겠습니까?");
      if (!boolRes) {
        console.log("삭제 취소");
        return;
      }

      const res = await jwtAxios.delete(
        `${BACK_BASIC_URL}/api/admin/board/notice/delete/${noticeId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      alert("삭제가 완료되었습니다.");
      navigate("/admin/noticeList");
    } catch (err) {
      console.log("삭제 실패, ", err);
    }
  };

  useEffect(() => {
    AdminNoticeDetailFn();
  }, []);
  return (
    <div className="admin-notice-detail">
      <div className="admin-notice-detail-con">
        <div className="admin-notice-detail-content-title">
          <h1>{noticeDetail.title}</h1>
          <div className="admin-notice-detail-content-time">
            <span>작성시간: {formatDate(noticeDetail.createTime)}</span>
            {noticeDetail.updateTime != null ? (
              <span>수정시간: {formatDate(noticeDetail.updateTime)}</span>
            ) : null}
            <span>조회수 {noticeDetail.hit}</span>
          </div>
        </div>
        <div className="admin-notice-detail-content">
          {noticeDetail.content}
        </div>
        <div className="admin-notice-detail-bottom">
          <div className="admin-notice-detail-links">
            <Link to="/admin/noticeList">이전으로</Link>
            <Link to={`/notice/detail/${noticeDetail.id}`}>
              원본글 보러가기
            </Link>
          </div>
          <div className="admin-notice-detail-btn">
            <button
              className="admin-notice-edit-btn"
              onClick={() => {
                navigate(`/admin/notice/update/${noticeId}`);
              }}
            >
              수정
            </button>
            <button
              className="admin-notice-delete-btn"
              onClick={AdminNoticeDeleteFn}
            >
              삭제
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminNoticeDetailContainer;
