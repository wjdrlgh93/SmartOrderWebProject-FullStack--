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

     // JWT
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

    // this code for BackEnd Controller
    // const response = await axios.get("http://localhost:8088/api/board");
    // parameter init
    let params = { 
        page: page,

        subject: subject, 
        search: search ? search.trim() : null
    };
    try {
      // REQUEST Page Query Parameter :: URL
      
      // const response = await jwtAxios.get(`${API_BASE_URL}?page=${page}`, 
      const response = await jwtAxios.get(API_BASE_URL , 
               {
                    params: params,
                    headers: { Authorization: `Bearer ${accessToken}` },
                    withCredentials: true,
              });

      const data = response.data;

      // data Update
      setBoards(data.content || []);
      // Page Calculate & update 
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
        // if compoent loading DONE ... URL has searchWord -> do search
        if (initialSearchTerm) {
            fetchSearchResults(initialSubject, initialSearchTerm, initialPage);
        }
    }, []); // 최초 1회만 실행



  // search Section /// 

  
  const handleSearch = (e) => {
        e.preventDefault(); // 기본 폼 제출 동작 방지
        
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


  // return

  return (
    <div className="boardList">
       <div className="boardList-banner">
        <br /><br />
        {/* <img src="/images/store/swiper/header3.jpg" alt="header" /> */}
        
      </div>
      <br />
      <div className="boardList-con">
     
        <h2>:: 자유게시판 ::</h2>
        <br /><br />
        <div className="searchBox">
          <form onSubmit={handleSearch} className="board-search-form">
            <select name="subject" className="subject-select"value={subject} 
                onChange={handleSubjectChange} >
                {/* option value must Math BackEndxcode.... */}
                <option value="title">제목</option>
                <option value="content">내용</option>
                <option value="nickName">닉네임</option>
            </select>
            <input type="text" name="search" value={searchTerm}
                onChange={handleSearchTermChange} // 변경 시 상태 업데이트
                placeholder="검색어를 입력하세요.." 
                className="search-input"/>
            <button type="submit" className="search-button">검색</button>
        </form>
        <br />
        </div>
        <table className='board-table'>
          <thead>
            <tr>
              <th scope='col'>ID</th>
              <th scope='col'>Image</th>
              <th scope='col'>:: 글제목</th>
              <th scope='col'>:: 작성자</th>
              <th scope='col'>:: 조회수</th>
              <th scope='col'>:: 파일</th>
            </tr>
          </thead>
          <tbody>
            {console.log(boards)}

            {boards.map(list => (
              <tr key={list.id}>
                <td>{list.id}</td>
                {/* <td>{list.fileUrl}</td> */}
                <td><img src={list.fileUrl} alt={list.newFileName} style={{ width: '40px', height: '40px', objectFit: 'cover' }}/></td>
                <td> <Link to={`/board/detail/${list.id}`} className='board-link'>
                  {list.title}
                </Link>
                </td>
                <td>{list.memberNickName}</td>
                <td>{list.hit}</td>
                <td>{list.attachFile}</td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className="pagenation">
          {/* PREV Page Button*/}
          {pageInfo.startPage > 0 && (
            <li style={{ margin: '0 5px' }}>
              <button
                onClick={() => handlePageClick(pageInfo.startPage - 1)}
                style={{ padding: '5px 10px', cursor: 'pointer' }}
              >
                &laquo; 이전
              </button>
            </li>
          )}

          {/* Page */}
          {pageNumbers.map(page => (
            <li key={page} style={{ margin: '0 5px' }}>
              <button
                onClick={() => handlePageClick(page)}
                // 현재 페이지일 경우 배경색을 회색으로 표시 (뼈대 스타일)
                style={{
                  padding: '5px 10px',
                  cursor: 'pointer',
                  fontWeight: page === pageInfo.currentPage ? 'bold' : 'normal',
                  backgroundColor: page === pageInfo.currentPage ? '#eee' : 'white'
                }}
              >
                {page + 1} {/* 사용자에게는 1부터 시작하는 페이지 번호를 보여줌 */}
              </button>
            </li>
          ))}

          {/* 다음 (Next) 버튼: 현재 블록의 끝 페이지(endPage)의 다음 페이지로 이동 */}
          {pageInfo.endPage < pageInfo.totalPages - 1 && (
            <li style={{ margin: '0 5px' }}>
              <button
                onClick={() => handlePageClick(pageInfo.endPage + 1)}
                style={{ padding: '5px 10px', cursor: 'pointer' }}
              >
                다음 &raquo;
              </button>
            </li>
          )}

          {/* EOF Pagenation */}

          <br />
        </div>
        <div className="boardList-post">
          {/* this section is temp */}
          <Link to="/board/newPost">
            <h3>글쓰기</h3>
          </Link>
          {/* { when Loggin 
            <>
              <Link to="/board/newPost">
                <h3>글쓰기</h3>
              </Link>
            </>
            :
            <>
              <Link to="/auth">
                <h3>로그인하세요...</h3>
              </Link>
            </>
          } */}

        </div>
        <br /><br /><br />
      </div>


    </div>
  )
}

export default BoardListContainer