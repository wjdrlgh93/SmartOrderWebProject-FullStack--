import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";

import "../../../../css/admin/container/AdminBoardDetailContainer.css"

const AdminBoardDetailContainer = () => {
  const { boardId } = useParams();
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [board, setBoard] = useState(null);

  const fetchBoardDetail = async () => {
    console.log("accessToken ===>", accessToken);
    try {
      const res = await jwtAxios.get(`${BACK_BASIC_URL}/api/admin/board/detail/${boardId}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      }
      );
      console.log("상세조회 성공", res.data);
      setBoard(res.data);
    } catch (error) {
      console.error("상세조회실패:", error);
      alert("상세조회실패");
    }
  };

  const handleDelete = async () => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    try {
      await jwtAxios.delete(`${BACK_BASIC_URL}/api/admin/board/delete/${boardId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );
      alert("삭제완료");
      navigate("/admin/boardList");
    } catch (error) {
      console.error("삭제실패", error);
      alert("삭제실패");
    }
  };

  useEffect(() => {
    console.log("useEffect accessToken ===>", accessToken); // ★ 여기
    fetchBoardDetail();
  }, [boardId]);

  if (!board) return <p>로딩중...</p>

  return (
    <div className="admin-board-detail">
      <h2>게시물 상세 페이지</h2>

      <div className="admin-board-detail-con">
        <div className="detail-con-right">
          <div>미리보기:</div>
          {board.boardImgDtos && board.boardImgDtos.length > 0 ? (
            <div className="preview-area">
              {board.boardImgDtos.map((img) => (
                <img
                  key={img.id}
                  src={`${BACK_BASIC_URL}/upload/${img.newName}`}
                  alt={img.oldName}
                  width="250"
                  height="350"
                  className="preview-img"
                />
              ))}
            </div>
          ) : (
            <div>첨부 이미지 없음</div>
          )}
        </div>
        <div className="detail-con-left">
          <div>ID: {board.id}</div>
          <div>제목: {board.title}</div>
          <div>작성자: {board.nickname || board.memberId}</div>
          <div>조회수: {board.hit}</div>
          {/* <div>파일: {board.attachFile === 1 ? "O" : "X"}</div> */}
          <div>내용: {board.content}</div>
          <button onClick={() => handleDelete(board.id)}>게시물 삭제</button>
        </div>
      </div>
    </div>
  )
}

export default AdminBoardDetailContainer;
