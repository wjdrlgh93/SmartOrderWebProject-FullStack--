import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { useSelector } from "react-redux";
import AdminPagingComponent from "../../../common/AdminPagingComponent";
import { Link } from "react-router";

const AdminMemberListContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [memberList, setMemberList] = useState([]);
  const [pageData, setPageData] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [subject, setSubject] = useState("userEmail");
  const [search, setSearch] = useState("");

  const adminMemberListFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/member/memberList?page=${
          currentPage - 1
        }&subject=${subject}&search=${search}`,

        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );

      setMemberList(res.data.content);
      setPageData(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    adminMemberListFn();
  }, [currentPage]);

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  // 날짜 이쁘게 변환
  const formatDate = (dateStr) => {
    const d = new Date(dateStr);
    return d.toLocaleString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <>
      <div className="admin-memberList">
        <div className="admin-memberList-con">
          <div className="admin-memberList-header">
            <h1>회원 리스트</h1>
            <div className="admin-memberList-search">
              <select
                value={subject}
                onChange={(e) => {
                  setSubject(e.target.value);
                  console.log("selected:", e.target.value);
                }}
                name="subject"
                id="subject"
              >
                <option value="userEmail">이메일</option>
                <option value="userName">이름</option>
                <option value="nickName">닉네임</option>
              </select>
              <input
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key == "Enter") {
                    adminMemberListFn();
                  }
                }}
                type="text"
                placeholder="검색어를 입력하세요"
              />
              <button onClick={adminMemberListFn}>검색</button>
            </div>
          </div>

          <div className="admin-memberList-table-wrapper">
            <table className="admin-memberList-table">
              <thead>
                <tr>
                  <th>번호</th>
                  <th>이메일</th>
                  <th>이름</th>
                  <th>닉네임</th>
                  <th>권한</th>
                  <th>가입일</th>
                  <th>관리</th>
                </tr>
              </thead>
              <tbody>
                {memberList.map((el) => {
                  return (
                    <tr key={el.id}>
                      <td>{el.id}</td>
                      <td>{el.userEmail}</td>
                      <td>{el.userName}</td>
                      <td>{el.nickName}</td>
                      <td>{el.role}</td>
                      <td>{formatDate(el.createTime)}</td>
                      <td>
                        <Link to={`/admin/memberDetail/${el.id}`}>보기</Link>
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
    </>
  );
};

export default AdminMemberListContainer;
