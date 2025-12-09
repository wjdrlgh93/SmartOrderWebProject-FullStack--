import axios from "axios";
import React, { useEffect, useState } from "react";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { Link, useNavigate, useParams } from "react-router";
import { formatDate } from "../../../js/formatDate";

const NoticeDetailContainer = () => {
  const { noticeId } = useParams();
  const navigate = useNavigate();
  const [noticeDetail, setNoticeDetail] = useState({});

  const noticeDetailFn = async () => {
    try {
      const res = await axios.get(
        `${BACK_BASIC_URL}/api/notice/detail/${noticeId}`
      );
      console.log(res.data);
      setNoticeDetail(res.data);
    } catch (err) {
      console.log("공지글 상세조회 실패, ", err);
    }
  };

  useEffect(() => {
    noticeDetailFn();
  }, []);
  return (
    <div className="board-notice-detail">
      <div className="board-notice-detail-con">
        <div className="board-notice-detail-content-title">
          <h1>{noticeDetail.title}</h1>
          <div className="board-notice-detail-content-time">
            <span>작성시간: {formatDate(noticeDetail.createTime)}</span>
            {noticeDetail.updateTime != null ? (
              <span>수정시간: {formatDate(noticeDetail.updateTime)}</span>
            ) : null}
            <span>조회수 {noticeDetail.hit + 1}</span>
          </div>
        </div>
        <div className="board-notice-detail-content">
          {noticeDetail.content}
        </div>
        <div className="board-notice-detail-bottom">
          <Link to="/notice">이전으로</Link>
        </div>
      </div>
    </div>
  );
};

export default NoticeDetailContainer;
