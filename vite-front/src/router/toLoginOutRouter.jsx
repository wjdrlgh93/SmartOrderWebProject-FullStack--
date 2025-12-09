import React, { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div className="loading">Loading...</div>;

// Page
const JoinMemberPage = lazy(() =>
  import("../components/container/auth/AuthJoinContainer")
);
const LoginPage = lazy(() =>
  import("../components/container/auth/AuthLoginContainer")
);
const OAuthSuccessPage = lazy(() =>
  import("../components/container/auth/OAuthSuccessContainer")
);

export const toLoginOutRouter = () => {
  return [
    {
      // auth
      path: "",
      element: <Navigate replace to={"login"} />,
    },
    {
      // auth/login
      path: "login",
      element: (
        <Suspense fallback={Loading}>
          <LoginPage />
        </Suspense>
      ),
    },
    {
      path: "join",
      element: (
        <Suspense fallback={Loading}>
          <JoinMemberPage />
        </Suspense>
      ),
    },
    {
      path: "oauth/success",
      element: (
        <Suspense fallback={Loading}>
          <OAuthSuccessPage />
        </Suspense>
      ),
    },
  ];
};
