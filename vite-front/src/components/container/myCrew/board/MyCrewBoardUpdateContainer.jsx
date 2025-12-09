import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux'
import axios from 'axios';
import jwtAxios from '../../../../apis/util/jwtUtil';

const MyCrewBoardUpdateContainer = () => {
  const { crewId, boardId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);
  const { userEmail } = useSelector((state) => state.loginSlice);  // 닉네임이 슬라이스에 있으면 좋겠당
  const nickName = useSelector(state => state.loginSlice.nickName);

  const [board, setBoard] = useState([]);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [newImages, setNewImages] = useState([]);
  const [exFiles, setExFiles] = useState([]);
  const [deleteImageName, setDeleteImageName] = useState([]);

  console.log(nickName)
  useEffect(() => {
      const fetchBoard = async () => {
        try {
            const res = await axios.get(`/api/mycrew/${crewId}/board/detail/${boardId}`);
            setBoard(res.data.boardDetail);
            setTitle(res.data.boardDetail.title);
            setContent(res.data.boardDetail.content);
            if (res.data.boardDetail.newFileName && res.data.boardDetail.newFileName.length > 0) {
                setExFiles(
                    res.data.boardDetail.newFileName.map((name, idx) => ({
                        newFileName: name,
                        fileUrl: res.data.boardDetail.fileUrl[idx]
                    }))
                );
            };
        } catch (err) {
            console.error("게시물 불러오기 실패", err)
        }
      };
      fetchBoard();
  }, [crewId, boardId])

  console.log(exFiles)

  const deleteImage = (name) => {
    setDeleteImageName(prev => [...prev, name]);
    setExFiles(prev => prev.filter(img => img.newFileName !== name));
  }

  const uploadNewFile = (e) => {
    setNewImages([...e.target.files])
  };

  const update = async (el) => {

    el.preventDefault();
    try {
      const formData = new FormData();
      formData.append('title', title)
      formData.append('content', content)
      newImages.forEach(image => formData.append('newImages', image))
      deleteImageName.forEach(name => formData.append('deleteImageName[]', name))

      const response = await jwtAxios.put(`/api/mycrew/${crewId}/board/update/${boardId}`,
        formData,
        { headers: {
            "Authorization": `Bearer ${accessToken}`,
            "Content-Type": "multipart/form-data"
            },
            withCredentials: true,
        }
      )
      alert('게시글 수정 완료')
      navigate(`/mycrew/${crewId}/board/detail/${boardId}`)
    } catch (err) {
      console.log(err)
      alert("게시글 수정 오류 발생")
    }
};

  return (
    <div className="boardCreate">
        <div className="boardCreate-con">
            <form onSubmit={update}>
                <div className="boardTitle">
                    <label className="crewBoardLabel">제목</label>
                    <input 
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                        placeholder='제목'
                    />
                </div>
                <div className="boardContent">
                    <label className="crewBoardLabel">내용</label>
                    <textarea 
                        name="content" 
                        id="content"
                        value={content}
                        onChange={(el) => setContent(el.target.value)}
                        required
                        placeholder='내용'
                    />
                </div>
                <div className="boardFile">
                    <span>기존 이미지</span>
                    <ul className='exFile'>
                        {exFiles.length > 0 ? (
                            exFiles.map((img, idx) => (
                            <li key={idx} className='exFileItem'>
                                {/* <img src={`http://localhost:8088/upload/${img.newFileName}`} alt={img.newFileName} /> */}
                                <img src={img.fileUrl} alt={img.originalFileName} className='exFiles'  style={{ width: '200px', height: 'auto' }} />
                                <button type='button' onClick={() => deleteImage(img.newFileName)}>✖</button>
                            </li>
                        ))
                    ) : (
                        <p>이미지 없음</p>
                    )}
                    </ul>
                    <span>새 이미지 업로드</span>
                    <input type="file" name='newImages' onChange={uploadNewFile} multiple/>
                </div>
                <div className="boardCreater">
                    <label className="crewBoardLabel">작성자</label>
                    <input type="text" value={nickName} readOnly/>
                </div>
                <div className="crewBoardCreateBtn">
                    <button className="crewBoardCreate" type="submit">수정완료</button>
                </div>
            </form>
        </div>
    </div>
  )
}

export default MyCrewBoardUpdateContainer