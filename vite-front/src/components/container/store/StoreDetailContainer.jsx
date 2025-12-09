import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import jwtAxios from "../../../apis/util/jwtUtil";
import { useSelector } from "react-redux";

import "../../../css/store/storeDetail.css";

const ShopDetailContainer = () => {
  // JWT
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const memberId = useSelector((state) => state.loginSlice.id);
  const nickName = useSelector((state) => state.loginSlice.nickName);

  const [item, setItem] = useState({});
  const [content, setContent] = useState("");
  const [replies, setReplies] = useState([]);

  // Editing
  const [editingReplyId, setEditingReplyId] = useState(null);
  const [editingContent, setEditingContent] = useState("");

  const [pageInfo, setPageInfo] = useState({
    // 페이지네이션 정보 상태 (first: true 추가)
    page: 0,
    size: 10,
    totalPages: 0,
    totalElements: 0,
    last: true,
    first: true,
  });

  const { id } = useParams();
  const navigate = useNavigate();

  const REPLY_BASE_URL = "http://localhost:8088/api/itemReply";
  const API_BASE_URL = "http://localhost:8088/api/shop";
  const IMAGE_BASE_URL = "http://localhost:8088/upload/";
  const NO_IMAGE_URL = "/images/noimage.jpg";

  const formatDate = (dateString) => {
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const fetchData = async () => {
    const response = await jwtAxios.get(`${API_BASE_URL}/detail/${id}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
      withCredentials: true,
    });

    if (response.data) {
      setItem(response.data);
      fetchReplies(response.data.id, 0, pageInfo.size);
    } else {
      console.log("게시물 데이터가 존재하지 않음.");
    }
  };

  const fetchReplies = async (itemId, page = 0, size = 10) => {
    if (!item) return;

    try {
      const response = await jwtAxios.get(
        `${REPLY_BASE_URL}/list/${itemId}?page=${page}&size=${size}&sort=createTime,desc`
      );

      setReplies(response.data.content);
      setPageInfo({
        page: response.data.pageable.pageNumber,
        size: response.data.pageable.pageSize,
        totalPages: response.data.totalPages,
        totalElements: response.data.totalElements,
        last: response.data.last,
        first: response.data.first,
      });
    } catch (error) {
      console.log("댓글 목록 조회 실패: ", error);
      setReplies([]);
    }
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < pageInfo.totalPages) {
      fetchReplies(item.id, newPage, pageInfo.size);
    }
  };
  // const handleUpdatePost = (itemId) => {
  //     navigate(`/store/update/${itemId}`);
  // }

  const handleReplyUpdate = async (replyId, currentContent) => {
    setEditingReplyId(replyId);
    setEditingContent(currentContent);
  };
  // Editing Reply Section
  const handleReplyEditCancel = () => {
    setEditingReplyId(null);
    setEditingContent("");
  };

  const handleReplyEditSubmit = async (replyId) => {
    if (!editingContent.trim()) {
      alert("수정할 내용을 입력해주세요.");
      return;
    }
    const updatedReplyData = {
      id: replyId,
      itemId: item.id,
      content: editingContent.trim(),
      memberId: memberId, // 권한 확인을 위해 현재 로그인된 사용자 ID 전송
    };
    console.log("전송할 댓글 수정 데이터:", updatedReplyData);
    console.log("전송 memberId:", updatedReplyData.memberId);
    try {
      const response = await jwtAxios.put(
        `${REPLY_BASE_URL}/updateReply`,
        updatedReplyData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );

      if (response.status === 200) {
        alert("댓글이 성공적으로 수정되었습니다.");
        handleReplyEditCancel(); // 수정 모드 종료
        // 현재 페이지의 댓글 목록을 갱신합니다.
        fetchReplies(item.id, pageInfo.page, pageInfo.size);
      } else {
        throw new Error("댓글 수정 요청 실패");
      }
    } catch (error) {
      console.error("댓글 수정 중 오류 발생:", error);
      const errorMessage =
        error.response?.data || "댓글 수정 중 오류가 발생했습니다.";
      alert(errorMessage); // 백엔드에서 던진 권한 에러 메시지 등을 사용자에게 표시
    }
  };

  const handleReplyDelete = async (replyId) => {
    if (!window.confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
      return;
    }
    if (!item.memberId) {
      alert("삭제 권한 확인을 위한 로그인 정보가 없습니다.");
      return;
    }
    try {
      const response = await jwtAxios.delete(
        `${REPLY_BASE_URL}/deleteReply/${replyId}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
          params: {
            memberId: memberId, // 쿼리 파라미터로 memberId 전송
          },
          withCredentials: true, // 4. 자격 증명 포함 설정
        }
      );

      if (response.status === 200 || response.status === 204) {
        alert("댓글이 성공적으로 삭제되었습니다.");
        // 댓글 삭제 후 현재 페이지의 댓글 목록을 갱신합니다.
        fetchReplies(item.id, pageInfo.page, pageInfo.size);
      } else {
        throw new Error("댓글 삭제 요청 실패");
      }
    } catch (error) {
      console.error("댓글 삭제 중 오류 발생:", error);
      const errorMessage =
        error.response?.data?.message || "댓글 삭제 중 오류가 발생했습니다.";
      alert(errorMessage); // 백엔드에서 던진 권한 에러 메시지 등을 사용자에게 표시
    }
  };

  const handleReplySubmit = async (e) => {
    e.preventDefault();

    if (!item.id || !content.trim() || !item.memberId) {
      alert("댓글 내용 및 작성자 정보가 필요합니다.");
      return;
    }

    const replyData = {
      itemId: item.id,
      content: content.trim(),
      memberId: memberId,
    };
    console.log("전송할 댓글 데이터:", replyData);

    try {
      const response = await jwtAxios.post(
        `${REPLY_BASE_URL}/addReply`,
        replyData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );

      if (response.status === 200) {
        alert("댓글이 성공적으로 등록되었습니다.");
        setContent("");
        fetchReplies(item.id, 0, pageInfo.size);
      } else {
        throw new Error("댓글 등록 실패 ");
      }
    } catch (error) {
      console.error("댓글 등록 중 오류 발생:", error);
      alert("댓글 등록 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }
  };

  // 장바구니 담기 함수 추가
  const handleAddToCart = () => {
    const product = {
      id: item.id,
      title: item.itemTitle,
      price: item.itemPrice,
      size: item.itemSize,
      image: item.attachFile,
      detail: item.itemDetail,
    };

    navigate("/cart", { state: { itemToAdd: product } });
  };

  useEffect(() => {
    fetchData();
  }, [id]);

  return (
    <div className="itemDetail">
      {console.log(item)}
      {console.log(replies)}

      <div className="itemDetail-con">
        <div className="itemDetail-con-image">
          {console.log(item)}
          {/* {item.itemImgDtos && item.itemImgDtos.length > 0 && ( */}
          {item.itemImgDtos && item.itemImgDtos.length > 0 ? (
            item.itemImgDtos.map((imgDto) => (
              <img
                // bring File by NewName Field
                key={imgDto.id || imgDto.newName}
                src={item.fileUrl}
                alt={imgDto.oldName}
                style={{
                  maxWidth: "100%",
                  height: "auto",
                  display: "block",
                  margin: "10px 0",
                }}
              />
            ))
          ) : (
            <img
              src={NO_IMAGE_URL}
              alt="이미지 없음"
              width="250"
              height="250"
              className="item-image"
            />
          )}
        </div>
        <div className="itemDetail-con-info">
          {console.log(item)}
          <h4>{item.itemTitle}</h4>
          <span>상품ID : {item.id}</span>
          <span>카테고리: {item.category}</span>
          <br />
          <span>상세설명 : {item.itemDetail}</span>
          <br />
          <span>상품가격 : {item.itemPrice}</span>
          <br />
          <span>itemSize(temp) : {item.itemSize}</span>
          <br />
          <span>attachFile(temp) : {item.attachFile}</span>
          <br />
          <span>createTime : {item.createTime}</span>
          <br />
          <span>updateTime : {item.updatTime}</span>
          <br />

          <div className="item-add-cart">
            <button className="add-cart-btn" onClick={handleAddToCart}>
              장바구니 담기
            </button>
            <input
              type="number"
              id="quantity"
              name="quantity"
              min="1"
              max="9"
              defaultValue="1"
            ></input>
          </div>
        </div>
        <div className="itemDetail-con-reply">
          {/* 댓글 입력 폼 */}
          <form onSubmit={handleReplySubmit}>
            <textarea
              name="reply"
              id="reply"
              rows="4"
              required
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="댓글을 입력해주세요.."
            ></textarea>
            <button type="submit">댓글등록</button>
          </form>
        </div>

        {/* 댓글 목록 표시 */}
        <div className="replyList">
          <h5>댓글 ({pageInfo.totalElements})</h5>
          {replies.length > 0 ? (
            replies.map((reply) => (
              <div key={reply.id}>
                <div className="tab">
                  {console.log(reply)}
                  {console.log(editingReplyId)}
                  <p>
                    <strong>{`작성자 ID: ${reply.memberId}`}</strong>
                  </p>
                  <span className="reply-createTime">
                    {formatDate(reply.createTime)}
                  </span>
                </div>
                <p className="reply-key-content">{reply.content}</p>
                {/* 해당 댓글의 버튼이 눌려 ID가 상태에 저장되었을 때만 폼 표시 */}
                {reply.id === editingReplyId && (
                  <div className="reply-edit-form">
                    {console.log(reply)}
                    {console.log(editingReplyId)}
                    <textarea
                      value={editingContent}
                      onChange={(e) => setEditingContent(e.target.value)}
                      rows="3"
                    ></textarea>
                    <div className="reply-edit-buttons">
                      <button onClick={() => handleReplyEditSubmit(reply.id)}>
                        수정
                      </button>
                      <button onClick={handleReplyEditCancel}>취소</button>
                    </div>
                  </div>
                )}
                {/*conditional rendering below here */}
                {reply.memberId === memberId && (
                  <div className="reply-actions">
                    {console.log(reply)}
                    <button
                      onClick={() => handleReplyUpdate(reply.id, reply.content)}
                    >
                      수정{" "}
                    </button>
                    {/* reply.id -> send delete request. */}
                    <button onClick={() => handleReplyDelete(reply.id)}>
                      삭제{" "}
                    </button>
                  </div>
                )}
              </div>
            ))
          ) : (
            <p className="reply-key-content-none">등록된 댓글이 없습니다.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default ShopDetailContainer;
