import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router';
import jwtAxios from '../../../apis/util/jwtUtil';

const MyCrewUpdateContainer = () => {
    const { crewId } = useParams();
    const navigate = useNavigate();
    const { accessToken } = useSelector((state) => state.jwtSlice);
    const loginMemberId = useSelector(state => state.loginSlice.id)
    
    const [crew, setCrew] = useState({})
    const [crewName, setCrewName] = useState('')
    const [description, setDescription] = useState('')
    const [district, setDistrict] = useState('')
    const [newImages, setNewImages] = useState([])
    const [exFiles, setExFiles] = useState([])
    const [deleteImageName, setDeleteImageName] = useState([])
    
    const districtsInSeoul = [
      "강남구", "강동구", "강북구", "강서구", "관악구",
      "광진구", "구로구", "금천구", "노원구", "도봉구",
      "동대문구", "동작구", "마포구", "서대문구", "서초구",
      "성동구", "성북구", "송파구", "양천구", "영등포구",
      "용산구", "은평구", "종로구", "중구", "중랑구"
    ];
    const selectChange = (el) => {
      setDistrict(el.target.value);
    }

    useEffect(() => {
      const fetchCrewDetail = async () => {
        try {
          const res = await jwtAxios.get(`/api/crew/detail/${crewId}`,
            {
                headers: { Authorization: `Bearer ${accessToken} `}
            }
          );
          setCrew(res.data.crewDetail);
          setCrewName(res.data.crewDetail.name)
          setDescription(res.data.crewDetail.description)
          setDistrict(res.data.crewDetail.district)
          console.log(res.data.crewDetail);
          if (res.data.crewDetail.newFileName && res.data.crewDetail.newFileName.length > 0) {
            setExFiles(
                res.data.crewDetail.newFileName.map((name, idx) => ({
                    fileUrl: res.data.crewDetail.fileUrl[idx],
                    newFileName : name
                }))
            );
          };
        } catch (err) {
          console.error("크루 상세 불러오기 실패", err);
        }
      };
      fetchCrewDetail(crewId);
    }, [crewId]);
    
    const deleteImage = (name) => {
        setDeleteImageName(prev => [...prev, name]);
        setExFiles(prev => prev.filter(img => img.newFileName !== name))
    }

    const uploadNewFile = (e) => {
        setNewImages([...e.target.files])
    }

    const update = async (el) => {
        el.preventDefault();
        try {
            const formData = new FormData();
            formData.append("crewName", crewName);
            formData.append('description', description)
            formData.append('district', district)
            newImages.forEach(image => formData.append('newImages', image))
            deleteImageName.forEach(name => formData.append('deleteImageName[]', name));

            const response = await axios.put(`http://localhost:8088/api/mycrew/${crewId}/update`,
                formData,
                {
                    headers: {
                        "Authorization": `Bearer ${accessToken}`
                    },
                }
            );
            alert('크루 수정 완료')
            navigate(`/mycrew/${crewId}`)
        } catch (err) {
            console.log(err);
            alert('크루 수정 실패')
        }
    }

    return (
        <div className="crewDetailHome">
          <div className="crewDetailHome-con">
            <form onSubmit={update}>
            <div className="crewUpdateImage">
                <span>기존 이미지</span>
                <ul>
                    {exFiles.length > 0 ? (
                        exFiles.map((img, idx) => (
                            <li key={idx}>
                                <img src={img.fileUrl} alt={idx} />
                                <button type='button' onClick={() => deleteImage(img.newFileName)}>✖</button>
                            </li>
                        ))
                    ) : (
                        <div>이미지 없음</div>
                    )}                
                </ul>
                <span>새 이미지 업로드</span>
                <input type="file" name='crewBoardImgaes' onChange={uploadNewFile} multiple />
            </div>
            <div className="crewIntroduction">
                <div className="crewInfo">
                    <label className="crewName">크루 이름</label>
                    <input 
                        type="text" 
                        value={crewName} 
                        onChange={(e) => setCrewName(e.target.value)}
                        required
                        placeholder="크루 이름"
                    />
                </div>
                <div className="description">
                    <label className="description">크루 소개</label>
                    <textarea 
                        name="description" 
                        id="messdescriptionage"
                        value={description}
                        onChange={(el) => setDescription(el.target.value)}
                        required
                        placeholder="크루 소개"
                    />
                </div>
                <div className="district">
                    <label className="crewDistrict">지역 선택</label>
                    <select name="district" id="district" value={district} onChange={selectChange}>
                        <option value="">지역선택</option>
                        {districtsInSeoul.map((d, index) => (
                            <option key={index} value={d}>
                                {d}
                            </option>
                        ))}
                    </select>
                    {district && <p>선택한 지역: {district}</p>}                 
                </div>
              <p>리더: {crew.memberNickName}</p>
              <p>멤버 {crew.crewMemberEntities?.length ?? 0}명</p>
            </div>
            <div className="crewUpdateBtn">
              <button className='crewUpdate' type='submit'>수정완료</button>
            </div>
            </form>
          </div>
        </div>
    );
}

export default MyCrewUpdateContainer