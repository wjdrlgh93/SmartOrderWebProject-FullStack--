import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useLocation, useNavigate } from 'react-router';
import jwtAxios from '../../../../apis/util/jwtUtil';
import { BACK_BASIC_URL } from '../../../../apis/commonApis';

import "../../../../css/admin/container/AdminBoardListContainer.css"

const AdminBoardListContainer = () => {

  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const keyword = params.get("keyword") || "";

  const [boards, setBoards] = useState([]);
  const [search, setsearch] = useState("title");

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [searchText, setSearchText] = useState("");

  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  // 검색
  const handleSearch = () => {
    if (!searchText.trim()) {
      alert("검색어를 입력하세요");
      return;
    }
    navigate(`/admin/boardList?keyword=${searchText}&search=${search}`);
  };

  const fetchBoards = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/board/boardList`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          params: { page: page, size: 10, keyword: keyword, search },
          withCredentials: true,
        }
      );
      setBoards(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (error) {
      console.log("게시판 목록 조회 실패", error);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("정말로 삭제하시겠습니까?")) return;

    try {
      await jwtAxios.delete(
        `${BACK_BASIC_URL}/api/admin/board/delete/${id}`,
        { headers: { Authorization: `Bearer ${accessToken}` } }
      );
      alert("삭제되었습니다");
      fetchBoards();
    } catch (error) {
      alert("삭제실패");
    }
  };

  // useEffect(() => {
  //   fetchBoards(keyword);
  // }, [keyword]);

  useEffect(() => {
    fetchBoards();
  }, [keyword, search, page]);

  return (
    <div className="admin-boardList">

      {/* 박스 전체 */}
      <div className="admin-boardList-con">

        {/* 제목 + 검색바 */}
        <div className="admin-boardList-header">
          <h1>게시판 목록 관리</h1>

          <div className="admin-boardList-search">
            <select
              value={search}
              onChange={(e) => setsearch(e.target.value)}
            >
              <option value="title">제목</option>
              <option value="content">내용</option>
              <option value="all">제목+내용</option>
            </select>

            <input
              type="text"
              value={searchText}
              placeholder="검색어를 입력하세요"
              onChange={(e) => setSearchText(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleSearch()}
            />

            <button onClick={handleSearch}>검색</button>
          </div>
        </div>

        {/* 테이블 */}
        <div className="admin-boardList-table-wrapper">
          <table className="admin-boardList-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>제목</th>
                <th>작성자ID</th>
                <th>닉네임</th>
                <th>조회수</th>
                <th>파일</th>
                <th>상세</th>
                <th>관리</th>
              </tr>
            </thead>

            <tbody>
              {boards.map((board) => (
                <tr key={board.id}>
                  <td>{board.id}</td>
                  <td>{board.title}</td>
                  <td>{board.memberId}</td>
                  <td>{board.memberNickName}</td>
                  <td>{board.hit}</td>
                  <td>{board.attachFile === 1 ? "O" : "X"}</td>

                  <td>
                    <button
                      onClick={() =>
                        navigate(`/admin/boardDetail/${board.id}`)
                      }
                    >
                      상세
                    </button>
                  </td>

                  <td>
                    <button className="delete-btn"
                      onClick={() => handleDelete(board.id)}>
                      삭제
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {/* 페이지네이션 */}
        <div className="pagination">
          {[...Array(totalPages)].map((_, i) => (
            <button
              key={i}
              onClick={() => setPage(i)}
              className={i === page ? "active" : ""}
            >
              {i + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AdminBoardListContainer;
