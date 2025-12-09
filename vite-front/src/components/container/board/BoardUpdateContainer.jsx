import axios from 'axios';
import React, { useCallback, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil';
import { useSelector } from 'react-redux';

import "../../../css/board/boardUpdate.css"


const BoardUpdateContainer = () => {
  // JWT
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const memberId = useSelector(state => state.loginSlice.id);
  const nickName = useSelector(state => state.loginSlice.nickName);

  const API_BASE_URL ='http://localhost:8088/api/board';
  const IMAGE_BASE_URL = 'http://localhost:8088/upload/';

  const { id } = useParams();

  const initialBoardState = {
    id: 0,
    memberId: 0,
    title: '',
    content: '',
    memberNickName: nickName,
  };
  const getInitialBoardState = useCallback(() => ({ 
          id: null, 
          memberId: memberId,
          title: '',
          content: '',
          memberNickName: nickName,
  }), [memberId, nickName]);

  const [boards, setBoards] = useState(initialBoardState);

  const navigate = useNavigate();

  const handleChange = (e) => {
    setBoards({
      ...boards,
      [e.target.name]: e.target.value
    });
  };

  const handleUpdateSubmit = async (e) => {
    e.preventDefault();
    console.log("Current boards state on submit:", boards);

    const formData = new FormData();
    formData.append('id', boards.id);
    formData.append('title', boards.title);
    formData.append('content', boards.content);
    formData.append('memberId', boards.memberId);

    const boardFile = e.target.boardFile.files[0];
    if (boardFile) {
      formData.append('boardFile', boardFile);
    }

    try {
      await jwtAxios.put(`${API_BASE_URL}/updatePost`, formData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
                    withCredentials: true,
        });
      alert(`${boards.id}번 게시물이 수정되었습니다`);
      navigate(`/board/detail/${boards.id}`); // 수정된 게시글 상세 페이지로 이동

    } catch (error) {
      console.error("게시물등록 실패!", error);
      alert("글쓰기 실패");

      if (error.response && error.response.data) {
        alert("수정 실패: " + error.response.data); // 서버 오류 메시지 출력 ("수정 권한이 없습니다." 등)
      } else {
        alert("게시물 수정 중 알 수 없는 오류가 발생했습니다.");
      }
    }
  };


  // Data Fetching
  const fetchData = useCallback(async () => {

    // there is NO Token ... Send Login...=>
    if (!accessToken) {
      navigate("/auth/login");
      return;
    }

    try {
      // id from parameter
      // GET /api/board/update/{id} Call
      const response = await jwtAxios.get(`${API_BASE_URL}/update/${id}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true
        });
   
      const data = response.data;
   
      


      // Set into Data <- Bring Data
      setBoards(data);
      console.log(data);
      console.log("서버 응답 데이터 타입:", typeof data); 
      console.log("서버 응답 데이터 내용:", data);
      console.log("Current boards state on submit:", boards);

      console.log(response.data.content)
    } catch (error) {
      console.error("게시물 조회 실패:", error.response);

      if (error.response) {
        if (error.response.status === 400) {
          alert(error.response.data);
          navigate(`/board/${id}`);
        } else if (error.response.status === 404) {
          alert("게시글 정보를 찾을 수 없습니다.");
          navigate("/board");
        } else {
          alert("서버 오류로 게시글 정보를 가져오지 못했습니다.");
        }
      } else {
        alert("네트워크 오류가 발생했습니다.");
      }
    }
  },[accessToken, id]);

  useEffect(() => {
    fetchData();

  }, [fetchData]);




  return (
    <div className="boardUpdate">

      <div className="boardUpdate-con">
        {console.log(boards)}
        <form onSubmit={handleUpdateSubmit} encType="multipart/form-data">
          <h4>:: 게시글수정하기 ::</h4>

          <div className="boardUpdate-con-img">
               {boards.boardImgDtos && boards.boardImgDtos.length > 0 && (
                            boards.boardImgDtos.map((imgDto) => (
                                <img
                                    // bring File by NewName Field
                                    key={imgDto.id || imgDto.newName}
                                    // src={`${IMAGE_BASE_URL}${imgDto.newName}`}
                                    src={boards.fileUrl}
                                    alt={imgDto.oldName}
                                    style={{ maxWidth: '100%', height: 'auto', display: 'block', margin: '10px 0' }}
                                />
                            ))
                        )}
          </div>
          <ul>
            <li className="first_li">
              <label htmlFor='memberId'>글 ID::</label>
              <input type="text" name="id" id="id" value={boards.id} readOnly />

              <label htmlFor='memberId'>memberId ID::</label>
              <input type="text" name="memberId" id="memberId" value={boards.memberId} readOnly />
            </li>
            <li>
              <label htmlFor="title">글제목::</label>
              {/* value & onChange Connection */}
              <input
                type="text"
                name="title"
                id="title"
                value={boards.title}
                onChange={handleChange}
                required
              />
            </li><br />
            <li>
              <label htmlFor="content">글내용::</label>
              {/* value & onChange Connection */}
              <textarea
                name="content"
                id="content"
                rows="10"
                value={boards.content}
                onChange={handleChange}
                required
              ></textarea>
            </li>

            <li>
              <label htmlFor="memberNickName">NickName::</label>
              {/* boards.memberNickName를 표시 */}
              <input type="text" name="memberNickName" id="memberNickName" value={boards.memberNickName} readOnly />
            </li>

            <li>
              <label htmlFor="boardFile">FILE (새 파일로 변경)</label>
              <input type="file" name="boardFile" id="boardFile" />
            </li>

            <li>
              <input type="submit" value="글수정" className="last" />
              <a href={`/board/detail/${boards.id}`} className="last">취소/돌아가기</a>
            </li>
          </ul>
        </form>

      </div>
    </div>
  )
}


export default BoardUpdateContainer