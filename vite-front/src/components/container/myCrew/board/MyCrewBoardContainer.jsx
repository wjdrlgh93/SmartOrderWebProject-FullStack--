import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import jwtAxios from '../../../../apis/util/jwtUtil';
import { useSelector } from 'react-redux';

const MyCrewBoardContainer = () => {
  const { crewId } = useParams();
  const navigate = useNavigate();
  const [crewBoardList, setCrewBoardList] = useState([]);
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [startPage, setStartPage] = useState(0)
  const [endPage, setEndPage] = useState(0)
  const [hasNext, setHasNext] = useState(false)
  const [hasPrevious, setHasPrevious] = useState(false)

  const [keyword, setKeyword] = useState('');
  const [subject, setSubject] = useState('전체')

  // 페이지 유지
  const [searchParams, setSearchParams] = useSearchParams()
  const [page, setPage] = useState(() => {
    const p = parseInt(searchParams.get('page'))
    return isNaN(p) ? 0 : p;
  })
  const pageChange = (newPage) => {
    setPage(newPage)
    searchParams.set('page', newPage)
    setSearchParams(searchParams)
  }

  const formattedDate = (el) => new Date(el).toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });

  
  const boardList = async () => {
    try {
      const res = await jwtAxios.get(`/api/mycrew/${crewId}/board/list`, {
        params: { page, size, keyword, subject }},
        { headers: {
          Authorization: `Bearer ${accessToken}`
          },
          withCredentials: true,
        })
      const data = res.data.crewBoardList
        setCrewBoardList(data.content || []);
        console.log(data)
        console.log(data.content)
        setTotalPages(data.totalPages || 0);
        setStartPage(data.startPage)
        setEndPage(data.endPage)
        setHasNext(data.hasNext)
        setHasPrevious(data.hasPrevious)
    } catch(err) {
      console.error("크루 게시글 목록 실패", err)
    }
  }

  useEffect(() => {
    if (!crewId) return;
    boardList();
    window.scrollTo({top:0, behavior: 'smooth'})
  }, [crewId, page])

  const search = (e) => {
    e.preventDefault();
    setPage(0);
    boardList();
  }

  const create = () => navigate(`/mycrew/${crewId}/board/create/`)

  return (
    <>
    <div className="crewBoardList">
        <div className="crewBoardList-con">
            {/* <h1 className='crewBoard'>{crewBoardList[0].crewName} 게시판</h1> */}
            <h1 className='crewBoard'>게시판</h1>

            <form className='crewBoardSearch' onSubmit={search}>
              <select name="crewBoard" id="crewBoard" value={subject} onChange={(e) => setSubject(e.target.value)}>
                <option value="전체">전체</option>
                <option value="제목">제목</option>
                <option value="내용">내용</option>
                <option value="작성자">작성자</option>
              </select>
              <input 
                type="text" 
                placeholder='검색'
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
              />
              <button className='crewBoardSearch' type='submit'>🔍검색</button>
            </form>

            <button className='createCrewBoard' onClick={create}>✏️ 게시글 작성</button>

            {crewBoardList.length === 0 ? (
              <p>등록된 게시글이 없습니다.</p>
            ) : (
              <ul>
                {crewBoardList.map((board) => (
                      <li key={board.id}>
                          <Link to={`/mycrew/${crewId}/board/detail/${board.id}`}>
                              <div className="boardContent">
                                  <div className="crewBoard">
                                    <span className="crewBoardWriter">
                                    🏃‍♂️{board.memberNickName}💨
                                    </span>
                                    <span className="crewBoardCreateTime">
                                      {formattedDate(board.createTime)}
                                    </span>
                                  </div>
                                  <div className="crewBoardBottom">
                                    <div className="crewBoardContent">
                                      {board.newFileName.length>0 ? 
                                        <span>📷</span>
                                        :
                                        <span>📝</span>
                                      }
                                    </div>
                                    <div className="crewBoardTitle">
                                      {board.title}
                                    </div>
                                    {board.comments > 0 &&
                                      <span className='crewBoardComments'>[{board.comments}]</span>
                                    }
                                  </div>
                              </div>
                          </Link>
                      </li>                          
                ))}
            </ul>
            )}

          <div className="crewBoardPagination">
            <button onClick={() => pageChange(0)} disabled={page === 0}>처음</button>
            <button onClick={() => pageChange(page - 1)} disabled={!hasPrevious}>이전</button>
            {Array.from({ length: endPage - startPage + 1 }, (_, idx) => (
              <button
                key={idx}
                onClick={() => pageChange(startPage + idx - 1)}
                disabled={startPage + idx - 1 === page}
              >
                {startPage + idx}
              </button>
            ))}
            <button onClick={() => pageChange(page + 1)} disabled={!hasNext}>다음</button>
            <button onClick={() => pageChange(totalPages - 1)} disabled={page === totalPages - 1}>마지막</button>
          </div>
        </div>
    </div>
    </>
  )
}

export default MyCrewBoardContainer