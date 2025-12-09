import axios from "axios";
import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import AdminPagingComponent from "../../common/AdminPagingComponent";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { formatDate } from "../../../js/formatDate";
import { Link } from "react-router";

const NoticeListContainer = () => {
  const [noticeList, setNoticeList] = useState([]);
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");

  const noticeListFn = async () => {
    try {
      const res = await axios.get(`${BACK_BASIC_URL}/api/notice/noticeList`, {
        params: { page: currentPage - 1, size: 10, keyword: search },
        withCredentials: true,
      });
      setNoticeList(res.data.content);
      setPageData(res.data);
      console.log(res);
    } catch (err) {
      console.log("공지목록 조회 실패,", err);
    }
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    noticeListFn();
  }, [currentPage]);
  return (
    <div className="board-notice-list">
      <div className="board-notice-list-con">
        <div className="board-notice-list-header">
          <h1>공지사항</h1>
          <div className="board-notice-list-search">
            <input
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  setCurrentPage(1);
                  noticeListFn();
                }
              }}
              type="text"
              placeholder="제목, 내용으로 검색"
            />
            <button
              onClick={() => {
                setCurrentPage(1);
                noticeListFn();
              }}
            >
              <span>검색</span>
            </button>
          </div>
        </div>

        <div className="board-notice-list-info">
          <p className="total-count">
            전체 <strong>{pageData.totalElements || 0}</strong>건
          </p>
        </div>

        <div className="board-notice-list-table-wrapper">
          <table className="board-notice-list-table">
            <thead>
              <tr>
                <th className="col-id">번호</th>
                <th className="col-title">제목</th>
                <th className="col-author">작성자</th>
                <th className="col-date">작성일</th>
                <th className="col-hit">조회수</th>
              </tr>
            </thead>
            <tbody>
              {noticeList.length > 0 ? (
                noticeList.map((el) => (
                  <tr key={el.id}>
                    <td className="col-id">{el.id}</td>
                    <td className="col-title">
                      <Link to={`/notice/detail/${el.id}`}>{el.title}</Link>
                    </td>
                    <td className="col-author">관리자</td>
                    <td className="col-date">{formatDate(el.createTime)}</td>
                    <td className="col-hit">{el.hit}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="no-data">
                    등록된 공지사항이 없습니다.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

        <AdminPagingComponent
          pageData={pageData}
          onPageChange={hadlePageChange}
        />
      </div>
    </div>
  );
};

export default NoticeListContainer;
