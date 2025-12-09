import React, { useState } from "react";
import { authDeleteFn } from "../../../apis/auth/authDetail";
import { useNavigate } from "react-router";

const AuthDeleteModal = ({ setIsModal, crewMemberList }) => {
  const [isChecked, setIsChecked] = useState(false);
  const navigate = useNavigate();

  const crewRoleCheck = () => {
    const boolRes = crewMemberList.some((el) =>
      el.roleInCrew.includes("LEADER")
    );
    return boolRes;
  };

  const myPageDeleteFn = async () => {
    const boolRes = crewRoleCheck();
    console.log(boolRes);
    if (boolRes) {
      alert(
        "운영 중인 크루가 존재합니다. 계정 탈퇴 전 크루장 권한을 해제하거나 크루를 정리해 주세요."
      );
      return;
    }

    const res = await authDeleteFn();
    if (res.status === 200) {
      alert("탈퇴 완료되었습니다. 안녕히가세요.");
      navigate("/store/index");
    }
  };

  const modalHandler = () => {
    setIsModal(false);
  };

  const eventChecked = (e) => {
    setIsChecked(e.target.checked);
  };

  return (
    <div className="member-delete-modal">
      <div className="member-delete-modal-overlay">
        <div className="member-delete-modal-container">
          <div className="member-delete-modal-icon">
            <div className="member-delete-modal-icon-circle">
              <span className="member-delete-modal-icon-emoji">⚠️</span>
            </div>
          </div>

          <h2 className="member-delete-modal-title">회원 탈퇴</h2>

          <div className="member-delete-modal-warning">
            <p className="member-delete-modal-warning-text">
              회원 탈퇴 시,{" "}
              <strong className="member-delete-modal-warning-strong">
                회원님의 모든 데이터
              </strong>
              (활동 내역, 혜택 포함)는
              <strong className="member-delete-modal-warning-strong">
                완전히 삭제
              </strong>
              되며, 동일한 정보로의
              <strong className="member-delete-modal-warning-strong">
                재가입은 불가능
              </strong>
              합니다.
            </p>
          </div>

          <div className="member-delete-modal-notice">
            <div className="member-delete-modal-notice-item">
              <span className="member-delete-modal-notice-bullet">•</span>
              <span className="member-delete-modal-notice-text">
                모든 활동 내역 및 포인트가 삭제됩니다
              </span>
            </div>
            <div className="member-delete-modal-notice-item">
              <span className="member-delete-modal-notice-bullet">•</span>
              <span className="member-delete-modal-notice-text">
                누적된 혜택 및 쿠폰이 소멸됩니다
              </span>
            </div>
            <div className="member-delete-modal-notice-item">
              <span className="member-delete-modal-notice-bullet">•</span>
              <span className="member-delete-modal-notice-text">
                동일 정보로 재가입이 불가능합니다
              </span>
            </div>
          </div>

          <label className="member-delete-modal-checkbox-label">
            <input
              type="checkbox"
              id="member-delete-agree"
              className="member-delete-modal-checkbox-input"
              onClick={eventChecked}
            />
            <span className="member-delete-modal-checkbox-text">
              위 탈퇴 시 유의사항(보유 정보 삭제, 재가입 불가 등)을 모두
              확인하였으며, 이에 동의합니다.
            </span>
          </label>

          <div className="member-delete-modal-buttons">
            <button
              className="member-delete-modal-button member-delete-modal-button-cancel"
              onClick={modalHandler}
            >
              취소
            </button>
            <button
              id="delete-confirm-ok"
              className="member-delete-modal-button member-delete-modal-button-delete"
              type="button"
              onClick={myPageDeleteFn}
              disabled={!isChecked}
            >
              탈퇴하기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AuthDeleteModal;
