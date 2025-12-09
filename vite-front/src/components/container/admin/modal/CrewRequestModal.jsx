import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { useSelector } from "react-redux";
import { formatDate } from "../../../../js/formatDate";

const CrewRequestModal = ({ setIsModal, crewRequestId, setRefreshCount }) => {
  const [crewRequestDetail, setCrewRequestDetail] = useState({});
  const [crewStatus, setCrewStatus] = useState();

  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const modalClick = () => {
    setIsModal(false);
  };

  const adminCrewRequestDetailFn = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/crew/create/detail/${crewRequestId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      setCrewRequestDetail(res.data);
      setCrewStatus(res.data.status);
    } catch (err) {
      console.log("크루 요청 조회를 실패하였습니다. " + err);
    }
  };

  const crewApproved = async () => {
    try {
      await jwtAxios.post(
        `${BACK_BASIC_URL}/api/crew/create/request/approved?requestId=${crewRequestId}`,
        {},
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      alert("크루 승인 완료!");
      setIsModal(false);
      setRefreshCount((prev) => prev + 1);
    } catch (err) {
      console.log("수락 요청을 실패했습니다. " + err);
    }
  };

  const crewRejected = async () => {
    try {
      await jwtAxios.post(
        `${BACK_BASIC_URL}/api/crew/create/request/rejected?requestId=${crewRequestId}`,
        {},
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      alert("크루 거절 완료!");
      setIsModal(false);
      setRefreshCount((prev) => prev + 1);
    } catch (err) {
      console.log("거절 요청을 실패했습니다. " + err);
    }
  };

  useEffect(() => {
    adminCrewRequestDetailFn();
  }, []);

  console.log(crewRequestDetail);
  return (
    <>
      <div className="admin-crew-modal-overlay" onClick={modalClick}>
        <div
          className="admin-crew-modal-container"
          onClick={(e) => e.stopPropagation()}
        >
          {/* 헤더 */}
          <div className="admin-crew-modal-header">
            <h3 className="admin-crew-modal-title">
              {crewRequestDetail.crewName}
            </h3>
            <button className="admin-crew-modal-close" onClick={modalClick}>
              ✕
            </button>
          </div>

          {/* 컨텐츠 */}
          <div className="admin-crew-modal-content">
            {/* 지역 */}
            <div className="info-row">
              <div className="info-icon icon-location">📍</div>
              <div className="info-text">
                <p className="info-label">지역</p>
                <p className="info-value">{crewRequestDetail.district}</p>
              </div>
            </div>

            {/* 신청 메시지 */}
            <div className="message-box">
              <p className="info-label">신청 메시지</p>
              <p className="message-text">{crewRequestDetail.message}</p>
            </div>

            {/* 상태 */}
            <div className="info-row">
              <div className="info-icon icon-status">⏳</div>
              <div className="info-text">
                <p className="info-label">상태</p>
                <span
                  className={`status-badge status-${crewRequestDetail.status}`}
                >
                  {crewRequestDetail.status === "APPROVED"
                    ? "승인됨"
                    : crewRequestDetail.status === "REJECTED"
                    ? "거절됨"
                    : "대기중"}
                </span>
              </div>
            </div>

            {/* 신청일 */}
            <div className="info-row">
              <div className="info-icon icon-date">📅</div>
              <div className="info-text">
                <p className="info-label">신청일</p>
                <p className="info-value">
                  {formatDate(crewRequestDetail.createTime)}
                </p>
              </div>
            </div>

            {/* 버튼 */}
            {crewRequestDetail.status !== "APPROVED" && (
              <div className="button-group">
                <button type="button" className="btn btn-approve" onClick={crewApproved}>
                  ✓ 수락
                </button>
                <button type="button" className="btn btn-reject" onClick={crewRejected}>
                  ✕ 거절
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default CrewRequestModal;
