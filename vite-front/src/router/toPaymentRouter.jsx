import React, { lazy, Suspense } from 'react';

const Loading = <div className='loading'>Loading..</div>;
const PaymentPage = lazy(() => import('../components/container/payment/PaymentPage'));
const PaymentListPage = lazy(() => import('../components/container/payment/PaymentListPage'));
const PaymentApprovalPage = lazy(() => import('../components/container/payment/PaymentApprovalPage'));
const PaymentSuccessPage = lazy(() => import('../components/container/payment/PaymentSuccessPage'));
const PaymentFailPage = lazy(() => import('../components/container/payment/PaymentFailPage'));
const CartPage = lazy(() => import('../components/container/cart/CartPage')); // 결제 취소시

export default function toPaymentRouter() {
  return [
    {
      path: "", // /payment
      element: <Suspense fallback={Loading}><PaymentPage /></Suspense>
    },
    {
      path: "approval/:paymentId/:productPrice/:memberId",
      element: <Suspense fallback={Loading}><PaymentApprovalPage /></Suspense>
    },
    {
      path: "success",
      element: <Suspense fallback={Loading}><PaymentSuccessPage /></Suspense>
    },
    {
      path: "fail",
      element: <Suspense fallback={Loading}><PaymentFailPage /></Suspense>
    },
    {
      path: "list",
      element: <Suspense fallback={Loading}><PaymentListPage /></Suspense>
    },
    {
      path: "cancel",
      element: <Suspense fallback={Loading}><CartPage /></Suspense>
    }
  ];
}
