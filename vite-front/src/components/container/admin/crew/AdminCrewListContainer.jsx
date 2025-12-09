import React, { useEffect, useState } from "react";

import { formatDate } from "../../../../js/formatDate";
import AdminPagingComponent from "../../../common/AdminPagingComponent";
import CrewDetailModal from "../modal/CrewDetailModal";
import { useDebouncee } from "../../../../js/admin/useDebounce";
import { adminCrewListFn } from "../../../../apis/admin/adminCrewList";

const AdminCrewListContainer = () => {
  const [ademinCrewDetailId, setAdeminCrewDetailId] = useState();
  const [isModal, setIsModal] = useState(false);
  const [adminCrewList, setAdminCrewList] = useState([]);
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");

  const debouncedSearch = useDebouncee(search, 300);

  const adminCrewListPage = async () => {
    const res = await adminCrewListFn(currentPage, search);
    setAdminCrewList(res.data.content);
    setPageData(res.data);
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  const modalClick = (id) => {
    setIsModal(true);
    setAdeminCrewDetailId(id);
  };

  useEffect(() => {
    adminCrewListPage();
  }, [currentPage, debouncedSearch]);

  return (
    <>
      <div className="admin-crewList">
        <div className="admin-crewList-con">
          <div className="admin-crewList-header">
            <h1 onClick={adminCrewListFn}>크루 리스트</h1>
            <div className="admin-crewList-search">
              <input
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key == "Enter") {
                    adminCrewListPage();
                  }
                }}
                type="text"
                placeholder="검색어를 입력하세요"
              />
              <button onClick={adminCrewListFn}>검색</button>
            </div>
          </div>

          <div className="admin-crewList-table-wrapper">
            <table className="admin-crewList-table">
              <thead>
                <tr>
                  <th>번호</th>
                  <th>크루명</th>
                  <th>리더</th>
                  <th>크루인원</th>
                  <th>지역</th>
                  <th>생성시간</th>
                  <th>상세보기</th>
                </tr>
              </thead>
              <tbody>
                {adminCrewList.map((el) => {
                  return (
                    <tr key={el.id}>
                      <td>{el.id}</td>
                      <td>{el.name}</td>
                      <td>{el.memberNickName}</td>
                      <td>{el.crewMemberEntities.length}명</td>
                      <td>{el.district}</td>
                      <td>{formatDate(el.createTime)}</td>
                      <td>
                        <button
                          onClick={() => {
                            modalClick(el.id);
                          }}
                        >
                          보기
                        </button>
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
        <CrewDetailModal
          isModal={isModal}
          setIsModal={setIsModal}
          ademinCrewDetailId={ademinCrewDetailId}
        />
      ) : null}
    </>
  );
};

export default AdminCrewListContainer;
