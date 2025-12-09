import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";

const CrewDetailModal = ({ isModal, setIsModal, ademinCrewDetailId }) => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [adminCrewDetail, setAdminCrewDetail] = useState("");
  const modalClick = () => {
    setIsModal(false);
  };

  const adminCrewDetailFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/crew/detail/${ademinCrewDetailId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      setAdminCrewDetail(res.data);
      console.log(res.data);
    } catch (err) {
      console.log("크루 요청 조회를 실패하였습니다. " + err);
    }
  };

  useEffect(() => {
    adminCrewDetailFn();
  }, []);
  return (
    <>
      <div className="admin-crewDetail-modal-overlay" onClick={modalClick}>
        <div
          className="admin-crewDetail-modal-container"
          onClick={(e) => e.stopPropagation()}
        >
          <div className="admin-crewDetail-modal-header">
            <h3 className="admin-crewDetail-modal-title">
              {adminCrewDetail.name}
            </h3>
            <button
              className="admin-crewDetail-modal-close"
              onClick={modalClick}
            >
              ✕
            </button>
          </div>

          <div className="admin-crewDetail-modal-content">
            {adminCrewDetail.crewImageEntities?.length > 0 ? (
              <div className="info-img">
                <img
                  src={`${BACK_BASIC_URL}/upload/${adminCrewDetail.crewImageEntities[0].newName}`}
                  alt="크루사진"
                />
              </div>
            ) : (
              <div className="info-img">
                <img
                  src="https://dummyimage.com/200x200/cccccc/000000&text=No+Image"
                  alt="기본 프로필"
                />
              </div>
            )}
            <div className="info-dev-con">
              <div className="info-row">
                <div className="info-icon icon-status">😎</div>
                <div className="info-text">
                  <p className="info-label">리더</p>
                  <p className="info-value">{adminCrewDetail.memberNickName}</p>
                </div>
              </div>
              <div className="info-row">
                <div className="info-icon icon-location">📍</div>
                <div className="info-text">
                  <p className="info-label">지역</p>
                  <p className="info-value">{adminCrewDetail.district}</p>
                </div>
              </div>
              <div className="info-row">
                <div className="info-icon icon-location">🙎‍♂️</div>
                <div className="info-text">
                  <p className="info-label">인원</p>
                  <p className="info-value">
                    {adminCrewDetail.crewMemberEntities?.length}명
                  </p>
                </div>
              </div>
            </div>
            <div className="message-box">
              <p className="info-label">소개</p>
              <p className="message-text">{adminCrewDetail.description}</p>
            </div>
            <div className="crew-button-group">
              <button className="btn btn-deleted">✕ 크루삭제</button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default CrewDetailModal;
