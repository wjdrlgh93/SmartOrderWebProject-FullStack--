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


    axios
      .get(`http://localhost:8088/api/payments/approval/${paymentId}/${productPrice}/${memberId}`, {
        params: { pg_token: pgToken, productName }
      })
      .then(() => {

        navigate("/payment/success");
      })
      .catch(() => {

        navigate("/payment/fail");
      });
  }, [paymentId, productPrice, memberId, pgToken, productName, navigate]);

  return <div>결제 승인 중...</div>;
};

export default PaymentApprovalPage;
