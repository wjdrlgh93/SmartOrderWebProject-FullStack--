import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import AdminPagingComponent from "../../../common/AdminPagingComponent";
import { formatDate } from "../../../../js/formatDate";
import CrewRequestModal from "../modal/CrewRequestModal";
import { hasRequestStatus } from "../../../../slices/adminSlice";
import { useDebouncee } from "../../../../js/admin/useDebounce";
import { newCrewCreateRequestFn } from "../../../../apis/admin/adminCrewList";

const AdminCrewAllowContainer = () => {
  const [crewRequest, setCrewRequest] = useState([]);
  const [crewRequestId, setCrewRequestId] = useState();
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");
  const [isModal, setIsModal] = useState(false);
  const [refreshCount, setRefreshCount] = useState(0);

  const debouncedSearch = useDebouncee(search, 300);
  const dispatch = useDispatch();

  const statusText = {
    PENDING: "대기중",
    APPROVED: "승인",
    REJECTED: "거절",
  };

  // 검색
  const crewReqeustFn = async () => {
    const res = await newCrewCreateRequestFn(currentPage, search);
    const pendingRs = res.data.content.some((el) =>
      el.status.includes("PENDING")
    );

    dispatch(hasRequestStatus(pendingRs));
    setCrewRequest(res.data.content);
    setPageData(res.data);
  };

  const handleRefresh = () => {
    setSearch("");
    setCurrentPage(1);
    setRefreshCount((prev) => prev + 1);
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  const modalClick = (id) => {
    setIsModal(true);
    setCrewRequestId(id);
  };

  useEffect(() => {
    crewReqeustFn();
  }, [currentPage, debouncedSearch, refreshCount]);

  return (
    <>
      <div className="admin-crewRequestList">
        <div className="admin-crewRequestList-con">
          <div className="admin-crewRequestList-header">
            <h1
              onClick={() => {
                handleRefresh();
              }}
            >
              크루신청 리스트
              <img src="/images/admin/refreshImg.png" alt="새로고참" />
            </h1>
            <div className="admin-crewRequestList-search">
              <input
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key == "Enter") {
                    crewReqeustFn();
                  }
                }}
                type="text"
                placeholder="검색어를 입력하세요"
              />
              <button onClick={crewReqeustFn}>검색</button>
            </div>
          </div>

          <div className="admin-crewRequestList-table-wrapper">
            <table className="admin-crewRequestList-table">
              <thead>
                <tr>
                  <th>번호</th>
                  <th>크루명</th>
                  <th>지역</th>
                  <th>신청시간</th>
                  <th>상태</th>
                  <th>관리</th>
                </tr>
              </thead>
              <tbody>
                {crewRequest.map((el) => {
                  return (
                    <tr key={el.id}>
                      <td>{el.id}</td>
                      <td>{el.crewName}</td>
                      <td>{el.district}</td>
                      <td>{formatDate(el.createTime)}</td>
                      <td
                        style={{
                          color:
                            el.status === "PENDING"
                              ? "orange"
                              : el.status === "APPROVED"
                              ? "#3b82f6"
                              : el.status === "REJECTED"
                              ? "red"
                              : "black",
                        }}
                      >
                        {statusText[el.status] || el.status}
                      </td>
                      <td>
                        <button onClick={() => modalClick(el.id)}>보기</button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
          <AdminPagingComponent
            pageData={pageData}
            onPageChange={hadlePageChange}
          />
        </div>
      </div>
      {isModal == true ? (
        <CrewRequestModal
          isModal={isModal}
          setIsModal={setIsModal}
          crewRequestId={crewRequestId}
          setRefreshCount={setRefreshCount}
        />
      ) : null}
    </>
  );
};

export default AdminCrewAllowContainer;
