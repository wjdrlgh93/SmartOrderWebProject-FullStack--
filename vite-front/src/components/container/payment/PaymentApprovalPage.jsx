import { useEffect } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import axios from "axios";

const PaymentApprovalPage = () => {
  const navigate = useNavigate();
  const { paymentId, productPrice, memberId } = useParams();
  const [searchParams] = useSearchParams();
  const productName = searchParams.get("productName");
  const pgToken = searchParams.get("pg_token");

  useEffect(() => {
    if (!pgToken) {
      navigate("/payment/fail");
      return;
    }

    // 백엔드 승인 API 호출
    axios
      .get(`http://localhost:8088/api/payments/approval/${paymentId}/${productPrice}/${memberId}`, {
        params: { pg_token: pgToken, productName }
      })
      .then(() => {
        // 성공 시 성공 페이지로 이동
        navigate("/payment/success");
      })
      .catch(() => {
        // 실패 시 실패 페이지로 이동
        navigate("/payment/fail");
      });
  }, [paymentId, productPrice, memberId, pgToken, productName, navigate]);

  return <div>결제 승인 중...</div>;
};

export default PaymentApprovalPage;
