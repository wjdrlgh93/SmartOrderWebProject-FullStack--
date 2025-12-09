import React, { lazy, Suspense } from "react";
const Loading = <div className="loading">Loading...</div>;
const AuthDetailPage = lazy(() =>
  import("../components/container/myPage/AuthDetailContainer")
);
const AuthUpdatePage = lazy(() =>
  import("../components/container/myPage/AuthUpdateContainer")
);

const AuthPaymentPage = lazy(() =>
  import("../components/container/myPage/AuthPaymentContainer")
);

const AuthPaymentDetailPage = lazy(() =>
  import("../components/container/myPage/AuthPaymentDetailContainer")
);
export const toMyPageRouter = () => {
  return [
    {
      path: "",
      element: (
        <Suspense fallback={Loading}>
          <AuthDetailPage />
        </Suspense>
      ),
    },
    {
      path: "update",
      element: (
        <Suspense fallback={Loading}>
          <AuthUpdatePage />
        </Suspense>
      ),
    },

    {
      path: "payment",
      element: (
        <Suspense fallback={Loading}>
          <AuthPaymentPage />
        </Suspense>
      ),
    },
    {
      path: "payment/:paymentId",
      element: (
        <Suspense fallback={Loading}>
          <AuthPaymentDetailPage />
        </Suspense>
      ),
    },
  ];
};
