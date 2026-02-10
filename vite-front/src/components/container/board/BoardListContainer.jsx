import axios from 'axios';
import React, { useCallback, useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { Link, useLocation, useNavigate } from 'react-router-dom';




import "../../../css/board/boardIndex.css"
import jwtAxios from '../../../apis/util/jwtUtil';
import { BACK_BASIC_URL } from '../../../apis/commonApis';

const BoardListContainer = () => {

  const navigate = useNavigate();

  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);

  const API_BASE_URL = 'http://localhost:8088/api/board';

    const accessToken = useSelector(state => state.jwtSlice.accessToken);
    const memberId = useSelector(state => state.loginSlice.id);
    const nickName = useSelector(state => state.loginSlice.nickName);

  const initialSubject = searchParams.get('subject') || 'title';
  const initialSearchTerm = searchParams.get('search') || '';
  const initialPage = searchParams.get('page') || 0; 

  const [subject, setSubject] = useState(initialSubject);
  const [searchTerm, setSearchTerm] = useState(initialSearchTerm);
  const [searchResults, setSearchResults] = useState(null); 
  const [loading, setLoading] = useState(false);


  const [boards, setBoards] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({
    totalPages: 0,
    startPage: 0,
    endPage: 0
  })


  const fetchData = async (page, subject=null, search=null) => {

     console.log(`[LOG] 페이지 ${page + 1}의 데이터를 요청합니다. 검색 조건: ${subject} / ${search}`);


    let params = { 
        page: page,

        subject: subject, 
        search: search ? search.trim() : null
    };
    try {

      const response = await jwtAxios.get(API_BASE_URL , 
               {
                    params: params,
                    headers: { Authorization: `Bearer ${accessToken}` },
                    withCredentials: true,
              });

      const data = response.data;

      setBoards(data.content || []);

      const totalPages = data.totalPages;
      const pageNum = data.number;
      const displayPageNum = 5;

      const startPage = Math.floor(pageNum / displayPageNum) * displayPageNum;
      let endPage = startPage + displayPageNum - 1;
      if (endPage >= totalPages) {
        endPage = totalPages - 1;
      }

      setPageInfo({
        currentPage: pageNum,
        totalPages: totalPages,
        startPage: startPage,
        endPage: endPage,
      });
    } catch (error) {
      console.error("데이터 로드 실패:", error);
      setBoards([]);
    }



  };

  useEffect(() => {
    fetchData(currentPage, subject, searchTerm);
 }, [currentPage, subject, searchTerm]);


  const pageNumbers = [];
  for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
    pageNumbers.push(i);
  }

  const handlePageClick = (pageNumbers) => {
    console.log(pageNumbers);
    setCurrentPage(pageNumbers);
  }

  useEffect(() => {
        if (initialSearchTerm) {
            fetchSearchResults(initialSubject, initialSearchTerm, initialPage);
        }
    }, []); 





  const handleSearch = (e) => {
        e.preventDefault(); 
        
        if (!searchTerm.trim()) {
            alert("검색어를 입력해 주세요.");
            fetchData(0, null, null);
            return;
        }
        fetchData(0, subject, searchTerm);
    };
    const handleSubjectChange = useCallback((e) => {
        setSubject(e.target.value);
    }, []);
    const handleSearchTermChange = useCallback((e) => {
        setSearchTerm(e.target.value);
    }, []);




 return (
  <div className="boardList">
    <div className="boardList-con">
      <h2>자유게시판</h2>

      <div className="searchBox">
        <form onSubmit={handleSearch} className="board-search-form">
          <select 
            className="subject-select" 
            value={subject} 
            onChange={handleSubjectChange}
          >
            <option value="title">제목</option>
            <option value="content">내용</option>
            <option value="nickName">닉네임</option>
          </select>
          <input 
            type="text" 
            value={searchTerm}
            onChange={handleSearchTermChange} 
            placeholder="어떤 글을 찾으시나요?" 
            className="search-input"
          />
          <button type="submit" className="search-button">검색</button>
        </form>
      </div>

      <table className='board-table'>
        <tbody>
          {boards.map(list => (
            <tr key={list.id}>
              <td width="60px">
                <img 
                  src={list.fileUrl || '/images/noimage.jpg'} 
                  alt="thumbnail" 
                  style={{ width: '50px', height: '50px', borderRadius: '8px', objectFit: 'cover' }}
                />
              </td>
              <td>
                <Link to={`/board/detail/${list.id}`} className='board-link'>
                  {list.title}
                </Link>
                <div className="member-info">
                  {list.memberNickName} • 조회 {list.hit} • {list.id}
                </div>
              </td>
              <td align="right" style={{ color: '#94a3b8' }}>
                {/* 날짜 데이터가 있다면 여기에 배치 */}
                {list.attachFile ? '📎' : ''}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 페이지네이션 */}
      <ul className="pagenation">
        {pageInfo.startPage > 0 && (
          <li>
            <button onClick={() => handlePageClick(pageInfo.startPage - 1)}>이전</button>
          </li>
        )}

        {pageNumbers.map(page => (
          <li key={page}>
            <button
              onClick={() => handlePageClick(page)}
              className={page === pageInfo.currentPage ? 'active' : ''}
            >
              {page + 1}
            </button>
          </li>
        ))}

        {pageInfo.endPage < pageInfo.totalPages - 1 && (
          <li>
            <button onClick={() => handlePageClick(pageInfo.endPage + 1)}>다음</button>
          </li>
        )}
      </ul>

      <div className="boardList-post">
        <Link to="/board/newPost" className="write-btn">
          새 글 쓰기
        </Link>
      </div>
    </div>
  </div>
);
}
export default BoardListContainer