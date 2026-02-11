import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil';

import "../../../css/board/boardDetail.css"
import { useSelector } from 'react-redux';
import { BACK_BASIC_URL } from '../../../apis/commonApis'; 

import DOMPurify from 'dompurify';



const BoardDetailContainer = () => {

 
    const accessToken = useSelector(state => state.jwtSlice.accessToken);
    const memberId = useSelector(state => state.loginSlice.id);
    const nickName = useSelector(state => state.loginSlice.nickName);



    const [boards, setBoards] = useState({});
    const [content, setContent] = useState('');
    const [isUpdating, setIsUpdating] = useState(false);

   
    const [editingReplyId, setEditingReplyId] = useState(null);
    const [editingContent, setEditingContent] = useState('');

    const [replies, setReplies] = useState([]);
    const [pageInfo, setPageInfo] = useState({
        page: 0,
        size: 10,
        totalPages: 0,
        totalElements: 0,
        last: true,
        first: true, 
    });
    const { id } = useParams();
    const navigate = useNavigate();


    const REPLY_BASE_URL = 'http://localhost:8088/api/reply';
    const API_BASE_URL = 'http://localhost:8088/api/board';


    const formatDate = (dateString) => {
        if (!dateString) return '';
        return new Date(dateString).toLocaleString('ko-KR', {
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit'

        });
    }


    const fetchData = async () => {
        const response = await jwtAxios.get(`${API_BASE_URL}/detail/${id}`,
            {
                headers: { Authorization: `Bearer ${accessToken}` },
                withCredentials: true,
            });

        if (response.data) {
            setBoards(response.data);
            fetchReplies(response.data.id, 0, pageInfo.size);
        } else {
            console.log("게시물 데이터가 존재하지 않음.")
        }
    };


    const fetchReplies = async (boardId, page = 0, size = 10) => {
        if (!boardId) return;

        try {
            const response = await jwtAxios.get(
                `${REPLY_BASE_URL}/list/${boardId}?page=${page}&size=${size}&sort=createTime,desc`,
                {
                    headers: { Authorization: `Bearer ${accessToken}` },
                    withCredentials: true,
                });


            console.log("리스폰스 >>" + response);
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
            console.error('댓글 목록 조회 실패:', error);
            setReplies([]);
        }
    }

    const handleReplyUpdateStart = (replyId, currentContent) => {
        setEditingReplyId(replyId);             // "이 댓글을 수정창으로 바꿔라!" 명령
        setEditReplyContent(currentContent); // 입력창에 기존 댓글 내용을 미리 채워줌
};


    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < pageInfo.totalPages) {
            fetchReplies(boards.id, newPage, pageInfo.size);
        }
    };


    const handleDelete = async () => {

        if (!window.confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            return;
        }
        try {

            const response = await jwtAxios.delete(`${API_BASE_URL}/detail/${boards.id}`,
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}` 
                    },
                    withCredentials: true, 
                });

            if (response.status === 200) {
                alert('게시글이 성공적으로 삭제되었습니다.');
                navigate('/board/index');
            } else if (response.status === 404) {
                alert('삭제할 게시글을 찾을 수 없습니다.');
            } else {

                throw new Error(`삭제 실패: ${response.statusText}`);
            }
        } catch (error) {
            console.error('게시글 삭제 중 오류 발생:', error);
            alert('게시글 삭제 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }
    };

    const handleUpdatePost = (boardId) => {
        navigate(`/board/update/${boardId}`);
    }
    const handleReplyUpdate = async (replyId, currentContent) => {
        setIsUpdating(true);
        console.log(`수정 요청 시작: ID ${replyId}`);
        setEditingReplyId(replyId);
        setEditingContent(currentContent);

    }
    const handleReplyEditSubmit = async (replyId) => {
        if (!editingContent.trim()) {
            alert('수정할 내용을 입력해주세요.');
            return;
        }
        const updatedReplyData = {
            id: replyId,
            boardId: boards.id,
            content: editingContent.trim(),
            memberId: memberId 
        };
        console.log("전송할 댓글 수정 데이터:", updatedReplyData);
        try {
            const response = await jwtAxios.put(`${REPLY_BASE_URL}/updateReply`, updatedReplyData);

            if (response.status === 200) {
                alert('댓글이 성공적으로 수정되었습니다.');
                
                handleReplyEditCancel();
                fetchReplies(boards.id, pageInfo.page, pageInfo.size);
            } else {
                throw new Error("댓글 수정 요청 실패");
            }

        } catch (error) {
            console.error('댓글 수정 중 오류 발생:', error);
            const errorMessage = error.response?.data || '댓글 수정 중 오류가 발생했습니다.';
            alert(errorMessage);
        } finally {
            setIsUpdating(false);
        }

    }

   
    const handleReplyEditCancel = () => {
        setEditingReplyId(null);
        setEditingContent('');
    }

    const handleReplyDelete = async (replyId) => {
        if (!window.confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
            return;
        }
   
        try {
            const response = await jwtAxios.delete(`${REPLY_BASE_URL}/deleteReply/${replyId}`,
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    },
                    params: {
                      
                        memberId: memberId
                    },
                    withCredentials: true,
                });

            if (response.status === 200 || response.status === 204) {
                alert('댓글이 성공적으로 삭제되었습니다.');
              
                fetchReplies(boards.id, pageInfo.page, pageInfo.size);
            } else {
                throw new Error("댓글 삭제 요청 실패");
            }
        } catch (error) {
            console.error('댓글 삭제 중 오류 발생:', error);
            const errorMessage = error.response?.data?.message || '댓글 삭제 중 오류가 발생했습니다.';
            alert(errorMessage); 
        }

    }


    useEffect(() => {
        fetchData();
    }, [id]);





    const handleReplySubmit = async (e) => {
        e.preventDefault();

        if (!boards.id || !content.trim() || !boards.memberId) {
            alert('댓글 내용 및 작성자 정보가 필요합니다.');
            return;
        }
        const replyData = {
            boardId: boards.id,
            content: content.trim(),
            memberId: memberId
        };
        console.log("전송할 댓글 데이터:", replyData);
        try {
            const response = await jwtAxios.post(`${REPLY_BASE_URL}/addReply`, replyData,
                {
                    headers: { Authorization: `Bearer ${accessToken}` },
                    withCredentials: true
                });

            if (response.status === 200) {
                alert('댓글이 성공적으로 등록되었습니다.');
                setContent('');
                fetchReplies(boards.id, 0, pageInfo.size);
            } else {
                throw new Error("댓글 등록 실패 ");
            }
        } catch (error) {
            console.error('댓글 등록 중 오류 발생:', error);
            alert('댓글 등록 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }
    };


    return (
        <div className="board-wrapper">
            <div className="board-card">
                {/* 1. Header Area */}
                <div className="board-header">
                    <h2 className="post-title">{boards.title}</h2>
                    <div className="post-meta">
                        <span className="author">작성자: {boards.memberNickName}</span>
                        <span className="divider">|</span>
                        <span className="date">{formatDate(boards.createTime)}</span>
                        <span className="divider">|</span>
                        <span className="views">조회수: {boards.hit}</span>
                    </div>
                </div>

                {/* 2. Content Area */}
                <div className="board-body">
                    {/* HTML Content (Quill Editor) */}
                    <div 
                        className="post-content ql-editor"
                        dangerouslySetInnerHTML={{ 
                            __html: DOMPurify.sanitize(boards.content) 
                        }}
                    ></div>

                    {/* Attached Images */}
                    {boards.boardImgDtos && boards.boardImgDtos.length > 0 && (
                        <div className="post-images">
                            {boards.boardImgDtos.map((imgDto) => (
                                <img
                                    key={imgDto.id || imgDto.newName}
                                    // src={boards.fileUrl} // 주의: 실제 구현시 imgDto.fileUrl 등으로 변경 필요할 수 있음
                                    src={`${BACK_BASIC_URL}/upload/${imgDto.newName}`}
                                    alt={imgDto.oldName}
                                    className="attached-img"
                                />
                            ))}
                        </div>
                    )}
                </div>

                {/* 3. Reply Section */}
                <div className="reply-section">
                    <h5 className="reply-count">댓글 <span>{pageInfo.totalElements}</span></h5>
                    
                    {/* Reply Form */}
                    <form className="reply-form" onSubmit={handleReplySubmit}>
                        <textarea 
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            placeholder="소중한 댓글을 남겨주세요."
                            rows="3"
                        ></textarea>
                        <button type="submit" className="btn-reply-submit">등록</button>
                    </form>

                    {/* Reply List */}
                    <div className="reply-list">
                        {replies.length > 0 ? (
                            replies.map((reply) => (
                                <div key={reply.id} className="reply-item">
                                    <div className="reply-header">
                                        <span className="reply-author">{reply.memberNickName || reply.memberId}</span>
                                        <span className="reply-date">{formatDate(reply.createTime)}</span>
                                    </div>

                                    {/* View Mode vs Edit Mode */}
                                    {editingReplyId === reply.id ? (
                                        <div className="reply-edit-box">
                                            <textarea
                                                value={editingContent}
                                                onChange={(e) => setEditingContent(e.target.value)}
                                                rows="3"
                                            ></textarea>
                                            <div className="reply-edit-actions">
                                                <button className="btn-save" onClick={() => handleReplyEditSubmit(reply.id)}>저장</button>
                                                <button className="btn-cancel" onClick={handleReplyEditCancel}>취소</button>
                                            </div>
                                        </div>
                                    ) : (
                                        <div className="reply-content">
                                            {reply.content}
                                        </div>
                                    )}

                                    {/* Action Buttons (Only for Author) */}
                                    {reply.memberId === memberId && editingReplyId !== reply.id && (
                                        <div className="reply-actions">
                                            <button onClick={() => handleReplyUpdateStart(reply.id, reply.content)}>수정</button>
                                            <button onClick={() => handleReplyDelete(reply.id)}>삭제</button>
                                        </div>
                                    )}
                                </div>
                            ))
                        ) : (
                            <div className="no-replies">등록된 댓글이 없습니다.</div>
                        )}
                    </div>

                    {/* Pagination */}
                    {pageInfo.totalPages > 1 && (
                        <div className="pagination">
                            <button 
                                onClick={() => handlePageChange(pageInfo.page - 1)}
                                disabled={pageInfo.first}
                                className="page-btn"
                            >
                                &lt; 이전
                            </button>
                            <span className="page-info">{pageInfo.page + 1} / {pageInfo.totalPages}</span>
                            <button 
                                onClick={() => handlePageChange(pageInfo.page + 1)}
                                disabled={pageInfo.last}
                                className="page-btn"
                            >
                                다음 &gt;
                            </button>
                        </div>
                    )}
                </div>

                {/* 4. Board Actions (Edit/Delete) */}
                {boards.memberId === memberId && (
                    <div className="board-footer">
                        <button className="btn-outline" onClick={() => navigate('/board/index')}>목록</button>
                        <div className="right-actions">
                            <button className="btn-edit" onClick={() => handleUpdatePost(boards.id)}>수정</button>
                            <button className="btn-delete" onClick={handleDelete}>삭제</button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default BoardDetailContainer;