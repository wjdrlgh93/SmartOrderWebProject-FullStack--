import axios from 'axios';
import React, { useCallback, useEffect, useState, useMemo } from 'react'
import { Navigate, useNavigate, useParams } from "react-router-dom";
import jwtAxios from '../../../apis/util/jwtUtil';
import { useSelector } from 'react-redux';

// 1. React Quill 및 스타일 임포트
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css'; // 에디터 스타일

import "../../../css/board/boardWrite.css"

const BoardWriteContainer = () => {
  
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const memberId = useSelector(state => state.loginSlice.id);
  const nickName = useSelector(state => state.loginSlice.nickName);

  const API_BASE_URL = 'http://localhost:8088/api/board';
  const { id } = useParams();

  const getInitialBoardState = useCallback(() => ({
    id: null,
    memberId: memberId,
    title: '',
    content: '', // 에디터의 HTML 태그가 포함된 문자열이 들어갑니다.
    memberNickName: nickName,
  }), [memberId, nickName]);

  const [boards, setBoards] = useState(getInitialBoardState);

  const navigate = useNavigate();

  // 2. 일반 input 태그 변경 핸들러
  const handleChange = (e) => {
    setBoards({
      ...boards,
      [e.target.name]: e.target.value
    });
  };

  // 3. 에디터 전용 변경 핸들러 (Quill은 value를 직접 반환함)
  const handleEditorChange = (value) => {
    setBoards({
        ...boards,
        content: value
    });
  };

  // 4. 에디터 툴바 설정 (옵션)
  const modules = useMemo(() => ({
    toolbar: [
      [{ 'header': [1, 2, 3, false] }],
      ['bold', 'italic', 'underline', 'strike', 'blockquote'],
      [{ 'list': 'ordered'}, { 'list': 'bullet' }, { 'indent': '-1'}, { 'indent': '+1' }],
      ['link', 'image'],
      [{ 'align': [] }, { 'color': [] }, { 'background': [] }],          
      ['clean']
    ],
  }), []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // 유효성 검사: 태그를 제외한 텍스트만 있는지 확인하려면 추가 로직 필요
    if(boards.content.trim() === '' || boards.content === '<p><br></p>') {
        alert("내용을 입력해주세요.");
        return;
    }

    const formData = new FormData();
    formData.append('title', boards.title);
    formData.append('content', boards.content); // HTML 문자열이 전송됨

    // 파일 업로드 처리 (ref 또는 e.target 사용)
    const boardFile = document.getElementById('boardFile').files[0];
    if (boardFile) {
      formData.append('boardFile', boardFile);
    }

    // 작성자 정보가 필요하다면 추가 (백엔드 로직에 따라 다름)
    formData.append('memberId', memberId);
    formData.append('memberNickName', nickName);

    try {
      await jwtAxios.post(`${API_BASE_URL}/write`, formData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true
        });
      alert(`게시물이 등록되었습니다`);
      navigate("/board");

    } catch (error) {
      console.error("게시물등록 실패!", error.response);
      alert("글쓰기 실패: " + (error.response?.data || "오류 발생"));
    }
  };


  const fetchData = async () => {
    if (!accessToken) {
      navigate("/auth/login");
      return;
    }
    // id가 있을 때만 데이터 로드 (수정 모드 대비)
    if (id) {
       // 기존 로직 유지...
       // 주의: 수정 모드일 때 백엔드에서 가져온 HTML content를 boards.content에 넣으면
       // 에디터가 자동으로 그 내용을 보여줍니다.
    }
  }

  useEffect(() => {
    fetchData();
  }, [id]); // 의존성 배열 수정

  return (
    <div className="boardPost">
      <div className="boardPost-con">
        <form onSubmit={handleSubmit} encType="multipart/form-data">
          <h4>:: 게시글 작성 ::</h4>
          
          <div className="form-group-row">
            <label>작성자</label>
            <input type="text" value={nickName} readOnly className="input-readonly" />
            {/* memberId는 굳이 보여줄 필요 없다면 hidden으로 처리하거나 생략 */}
          </div>

          <div className="form-group">
            <label htmlFor="title">제목</label>
            <input 
                type="text" 
                name="title" 
                id="title" 
                value={boards.title || ''}
                onChange={handleChange} 
                required 
                placeholder="제목을 입력하세요"
                className="input-title"
            />
          </div>

          {/* 5. 에디터 영역 */}
          <div className="form-group editor-area">
            <label>내용</label>
            <ReactQuill 
                theme="snow" 
                value={boards.content} 
                onChange={handleEditorChange}
                modules={modules}
                placeholder="내용을 입력하세요..."
                style={{ height: '400px', marginBottom: '50px' }} // 높이 설정 중요
            />
          </div>

          <div className="form-group">
            <label htmlFor="boardFile">첨부파일</label>
            <input type="file" name="boardFile" id="boardFile" className="input-file"/>
          </div>

          <div className="button-group">
             <button type="submit" className="btn-submit">등록하기</button>
             <button type="button" onClick={() => navigate('/board')} className="btn-cancel">취소</button>
          </div>

        </form>
      </div>
    </div>
  )
}

export default BoardWriteContainer