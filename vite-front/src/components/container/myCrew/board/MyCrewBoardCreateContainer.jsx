import React, { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux'
import axios from 'axios';
import jwtAxios from '../../../../apis/util/jwtUtil';

const MyCrewBoardCreateContainer = () => {
  const { crewId } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useSelector((state) => state.jwtSlice);
  const { userEmail, nickName } = useSelector((state) => state.loginSlice);

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [files, setFiles] = useState([]);

  const fileChange = (e) => {
    const selectedFiles = Array.from(e.target.files)
    setFiles(exFiles => [...exFiles, ...selectedFiles])
  }

  const removeFile = (idx) => {
    setFiles(exFiles => exFiles.filter((_, i) => i != idx))
  }

  const create = async (el) => {
    el.preventDefault();

    try {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('content', content);

        if (files.length > 0) {
            for (let i = 0; i < files.length; i ++) {
                formData.append('crewBoardFile', files[i]);
            }
        } else if (!files || files.length == 0) {
                formData.append("crewBoardFile", new Blob([]), "");
        } 
        const response = await jwtAxios.post(`/api/mycrew/${crewId}/board/create`, 
            formData,
            {
                headers: {
                    "Authorization": `Bearer ${accessToken}`,
                },      
                withCredentials: true,        
            }
        );
        alert('게시글 작성 완료');
        navigate(`/mycrew/${crewId}/board/detail/${response.data.id}`);
        } catch (err) {
        console.log(err);
        alert("error: " + err.message);
    }
  }

  return (
    <div className="boardCreate">
        <div className="boardCreate-con">
            <form onSubmit={create}>
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
                        onChange={(e) => setContent(e.target.value)}
                        required
                        placeholder='내용'
                    />
                </div>
                <div className="boardFile">
                    <span>파일</span>
                    <input type="file" name='crewBoardFile' onChange={fileChange} multiple/>
                    {files.length > 0 && (
                        <ul className='exFile'>
                            {files.map((file, idx) => (
                                <li className='exFileItem' key={idx}>
                                    <img className='crewBoardFileImg' src={URL.createObjectURL(file)} alt={idx} />
                                    <button type='button' onClick={() => removeFile(idx)}>x</button>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
                <div className="boardCreater">
                    <label className="crewBoardLabel">작성자</label>
                    <input type="text" value={nickName} readOnly/>
                </div>
                <div className="crewBoardCreateBtn">
                    <button className="crewBoardCreate" type="submit">작성완료</button>
                </div>
            </form>
        </div>
    </div>
  )
}

export default MyCrewBoardCreateContainer