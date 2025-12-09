import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil';

import "../../../css/board/boardDetail.css"
import { useSelector } from 'react-redux';



const BoardDetailContainer = () => {

    // JWT
    const accessToken = useSelector(state => state.jwtSlice.accessToken);
    const memberId = useSelector(state => state.loginSlice.id);
    const nickName = useSelector(state => state.loginSlice.nickName);


    // boards 상태를 빈 객체로 초기화합니다.
    const [boards, setBoards] = useState({});
    const [content, setContent] = useState('');
    const [isUpdating, setIsUpdating] = useState(false);


    // Editing
    const [editingReplyId, setEditingReplyId] = useState(null);
    const [editingContent, setEditingContent] = useState('');

    const [replies, setReplies] = useState([]);
    const [pageInfo, setPageInfo] = useState({
        page: 0,
        size: 10,
        totalPages: 0,
        totalElements: 0,
        last: true,
        first: true, // 누락된 'first' 속성 복원
    });
    const { id } = useParams();
    const navigate = useNavigate();


    const REPLY_BASE_URL = 'http://localhost:8088/api/reply';
    const API_BASE_URL = 'http://localhost:8088/api/board';
    // const IMAGE_BASE_URL = 'http://localhost:8088/upload/';


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
            // 게시글 로드 성공 후, 댓글 목록 초기 로드 (1페이지)
            fetchReplies(response.data.id, 0, pageInfo.size);
        } else {
            console.log("게시물 데이터가 존재하지 않음.")
        }
    };

    // 댓글 목록을 불러오는 함수 (페이징 적용)
    const fetchReplies = async (boardId, page = 0, size = 10) => {
        if (!boardId) return;

        try {
            const response = await jwtAxios.get(
                `${REPLY_BASE_URL}/list/${boardId}?page=${page}&size=${size}&sort=createTime,desc`,
                {
                    headers: { Authorization: `Bearer ${accessToken}` },
                    withCredentials: true,
                });

            // 데이터와 페이지 정보 업데이트
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
            // axios.delete를 사용하여 DELETE 요청을 보냅니다.( 나 이 게시물 번호를 삭제하고 싶어요~ )
            const response = await jwtAxios.delete(`${API_BASE_URL}/detail/${boards.id}`,
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}` // 인증 헤더
                    },
                    withCredentials: true, // 자격 증명 포함
                    // 만약 백엔드에서 삭제 권한 확인을 위해 쿼리 파라미터를 요구한다면:
                    // params: {
                    //     memberId: boards.memberId 
                    // }
                });

            if (response.status === 200) {
                alert('게시글이 성공적으로 삭제되었습니다.');
                navigate('/board/index');
            } else if (response.status === 404) {
                alert('삭제할 게시글을 찾을 수 없습니다.');
            } else {
                // 서버에서 200/404 외의 상태 코드를 반환할 경우를 대비
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
            memberId: memberId // 권한 확인을 위해 현재 로그인된 사용자 ID 전송
        };
        console.log("전송할 댓글 수정 데이터:", updatedReplyData);
        try {
            const response = await jwtAxios.put(`${REPLY_BASE_URL}/updateReply`, updatedReplyData);

            if (response.status === 200) {
                alert('댓글이 성공적으로 수정되었습니다.');
                
                handleReplyEditCancel(); // 수정 모드 종료
                // 현재 페이지의 댓글 목록을 갱신합니다.
                fetchReplies(boards.id, pageInfo.page, pageInfo.size);
            } else {
                throw new Error("댓글 수정 요청 실패");
            }

        } catch (error) {
            console.error('댓글 수정 중 오류 발생:', error);
            const errorMessage = error.response?.data || '댓글 수정 중 오류가 발생했습니다.';
            alert(errorMessage); // 백엔드에서 던진 권한 에러 메시지 등을 사용자에게 표시
        } finally {
            setIsUpdating(false);
        }

    }

    // Editing Reply Section
    const handleReplyEditCancel = () => {
        setEditingReplyId(null);
        setEditingContent('');
    }

    const handleReplyDelete = async (replyId) => {
        if (!window.confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
            return;
        }
        // if (!reply.memberId === replyMemberId) {
        //     alert('댓글 삭제 권한이 없습니다. (작성자만 삭제 가능) :: handleReplyDelete');
        //     return;
        // }
        try {
            const response = await jwtAxios.delete(`${REPLY_BASE_URL}/deleteReply/${replyId}`,
                {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    },
                    params: {
                        // 3. 삭제 권한 확인을 위한 현재 사용자 ID를 쿼리 파라미터로 전송
                        memberId: memberId
                    },
                    withCredentials: true, // 4. 자격 증명 포함 설정
                });

            if (response.status === 200 || response.status === 204) {
                alert('댓글이 성공적으로 삭제되었습니다.');
                // 댓글 삭제 후 현재 페이지의 댓글 목록을 갱신합니다.
                fetchReplies(boards.id, pageInfo.page, pageInfo.size);
            } else {
                throw new Error("댓글 삭제 요청 실패");
            }
        } catch (error) {
            console.error('댓글 삭제 중 오류 발생:', error);
            const errorMessage = error.response?.data?.message || '댓글 삭제 중 오류가 발생했습니다.';
            alert(errorMessage); // 백엔드에서 던진 권한 에러 메시지 등을 사용자에게 표시
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

        <div className="upper-boardDetail">

            <div className="boardDetail">


                <div className="boardDetail-con-info">
                    <h2>{boards.title} </h2>
                    <h5> 작성자 : {boards.memberNickName} </h5>
                    <h6>조회수 : {boards.hit} </h6>
                    {/* formatDate 함수를 사용하여 날짜 포맷 적용 */}
                    <h5>작성일 : {formatDate(boards.createTime)} </h5>
                </div>

                <div className="boardDetail-con">
                    
                    {/* 게시글 본문 내용을 표시하는 부분 */}
                    <p className="boardDetail-content" style={{ whiteSpace: 'pre-wrap', marginBottom: '20px' }}>
                        {boards.content}
                    </p>
                    {console.log(boards)}
                    <div className="boardDetail-con-image">


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

                    {/* 댓글 섹션 */}
                    <div className="boardDetail-reply">

                        {/* 댓글 입력 폼 */}
                        <form onSubmit={handleReplySubmit}>
                            <textarea name="reply" id="reply"
                                rows="4" required
                                value={content}
                                onChange={(e) => setContent(e.target.value)}
                                placeholder='댓글을 입력해주세요..'
                            ></textarea>
                            <button type="submit">댓글 등록</button>
                        </form>


                        {/* 댓글 목록 표시 */}
                        <div className="reply-list">
                            <h5>댓글 ({pageInfo.totalElements})</h5>
                            {replies.length > 0 ? (
                                replies.map((reply) => (
                                    <div key={reply.id} className="reply-key">

                                        <div className="reply-key-sub">
                                            <p><strong>{`작성자 ID: ${reply.memberId}`}</strong></p>
                                            <span className="reply-key-createtime">{formatDate(reply.createTime)}</span>
                                        </div>
                                        <p className="reply-key-content">{reply.content}</p>
                                        {/* 해당 댓글의 버튼이 눌려 ID가 상태에 저장되었을 때만 폼 표시 */}
                                        {reply.id === editingReplyId && (


                                            <div className="reply-edit-form">
                                                {console.log(reply)}
                                                {console.log('editingReplyId >>' + editingReplyId)}
                                                {/* {console.log(editingReplyId)} */}
                                                <textarea
                                                    value={editingContent}
                                                    onChange={(e) => setEditingContent(e.target.value)}
                                                    rows="3"
                                                ></textarea>
                                                <div className="reply-edit-buttons">
                                                    <button
                                                        onClick={() => handleReplyEditSubmit(reply.id)}>
                                                        덧글수정
                                                    </button>
                                                    <button
                                                        onClick={handleReplyEditCancel}>
                                                        덧글수정취소
                                                    </button>
                                                </div>
                                            </div>
                                        )}
                                        { /*conditional rendering below here */}
                                        {!isUpdating && (
                                            <>
                                            {reply.memberId === memberId && (
                                                <div className="reply-actions">
                                                    {console.log(reply)}
                                                    <button onClick={() => handleReplyUpdate(reply.id, reply.content)}>
                                                        수정 </button>
                                                    { /* reply.id -> send delete request. */}
                                                    <button onClick={() => handleReplyDelete(reply.id)}>
                                                        삭제 </button>
                                                </div>

                                            )}
                                         </>
                                        )}
                                    </div>

                                ))
                            ) : (
                                < p className="reply-key-content-none">등록된 댓글이 없습니다.</p>
                            )}
                        </div>
                    </div>


                    {
                        pageInfo.totalPages > 1 && (
                            <div className="page-button-top">
                                <button
                                    onClick={() => handlePageChange(pageInfo.page - 1)}
                                    disabled={pageInfo.first}
                                    style={{ padding: '5px 10px', border: '1px solid #ccc', borderRadius: '5px' }}>
                                    이전
                                </button>
                                <span style={{ padding: '5px 10px', background: '#eee', borderRadius: '5px', fontWeight: 'bold' }}>
                                    {pageInfo.page + 1} / {pageInfo.totalPages}
                                </span>

                                <button
                                    onClick={() => handlePageChange(pageInfo.page + 1)}
                                    disabled={pageInfo.last}
                                    style={{ padding: '5px 10px', border: '1px solid #ccc', borderRadius: '5px' }}>
                                    다음
                                </button>
                            </div>
                        )
                    }

                </div>

                {/* 게시글 수정/삭제 버튼 */}
                { /*conditional rendering below here */}
                {boards.memberId === memberId && (
                    <div className="boardDetail-act">
                        <button onClick={() => handleUpdatePost(boards.id)}>게시글 수정</button>
                        <button onClick={handleDelete}>게시글 삭제</button>
                    </div>

                )}

            </div>
        </div >

    )
}



export default BoardDetailContainer