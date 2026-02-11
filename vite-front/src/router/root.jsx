import React, { lazy, Suspense } from "react";
import { createBrowserRouter } from "react-router-dom";

import { toLoginOutRouter } from "./toLoginOutRouter";
import toCrewRouter from "./toCrewRouter";
import toAdminRouter from "./toAdminRouter";
import toBoardRouter from "./toBoardRouter";
import toStoreRouter from "./toStoreRouter";
import toCartRouter from "./toCartRouter";
import toPaymentRouter from "./toPaymentRouter";
import toMyCrewRouter from "./toMyCrewRouter";
import { toMyPageRouter } from "./toMyPageRouter";
import toApiRouter from "./toApiRouter";
import { toNoticeRouter } from "./toNoticeRouter";

const Loading = <div className="loading">Loading..</div>;

// Layout
const StoreLayout = lazy(() => import(`../layout/StoreLayout`));
const LoginLayout = lazy(() => import(`../layout/LoginLayout`));
const MyPageLayout = lazy(() => import(`../layout/MyPageLayout`));
const AdminLayout = lazy(() => import(`../layout/admin/AdminLayout`));
const BoardLayout = lazy(() => import(`../layout/BoardLayout`));
const NoticeLayout = lazy(() => import(`../layout/NoticeLayout`));
const CrewLayout = lazy(() => import("../layout/CrewLayout"));

const CartLayout = lazy(() => import(`../layout/CartLayout`));
const PaymentLayout = lazy(() => import(`../layout/PaymentLayout`));

const MyCrewLayout = lazy(() => import("../layout/MyCrewLayout"));
const ApiLayout = lazy(() => import("../layout/ApiLayout"));

// Page
const IndexPage = lazy(() => import(`../pages/IndexPage`));

const root = createBrowserRouter([
  {
    //index
    path: "",
    element: (
      <Suspense fallback={Loading}>
        <IndexPage />
      </Suspense>
    ),
  },
  {
    // Admin
    path: "admin",
    element: (
      <Suspense fallback={Loading}>
        <AdminLayout />
      </Suspense>
    ),
    children: toAdminRouter(),
  },
  {
    // Auth
    path: "auth",
    element: (
      <Suspense fallback={Loading}>
        <LoginLayout />
      </Suspense>
    ),
    children: toLoginOutRouter(),
  },
  {
    path: "myPage",
    element: (
      <Suspense fallback={Loading}>
        <MyPageLayout />
      </Suspense>
    ),
    children: toMyPageRouter(),
  },
  {
    // shop
    path: "store",
    element: (
      <Suspense fallback={Loading}>
        <StoreLayout />
      </Suspense>
    ),
    children: toStoreRouter(),
  },
  {
    // crew
    path: "crew",
    element: (
      <Suspense fallback={Loading}>
        <CrewLayout />
      </Suspense>
    ),
    children: toCrewRouter(),
  },
  {
    // board ( Community )
    path: "board",
    element: (
      <Suspense fallback={Loading}>
        <BoardLayout />
      </Suspense>
    ),
    children: toBoardRouter(),
  },
  {
    // event ( 대회일정 )
    path: "notice",
    element: (
      <Suspense fallback={Loading}>
        <NoticeLayout />
      </Suspense>
    ),
    children: toNoticeRouter(),
  },
  {
    // cart
    path: "cart",
    element: (
      <Suspense fallback={Loading}>
        <CartLayout />
      </Suspense>
    ),
    children: toCartRouter(),
  },
  {
    // payment
    path: "payment",
    element: (
      <Suspense fallback={Loading}>
        <PaymentLayout />
      </Suspense>
    ),
    children: toPaymentRouter(),
  },
  {
    // mycrew
    path: "mycrew/:crewId",
    element: (
      <Suspense fallback={Loading}>
        <MyCrewLayout />
      </Suspense>
    ),
    children: toMyCrewRouter(),
  },
  {
    // api
    path: "open",
    element: (
      <Suspense fallback={Loading}>
        <ApiLayout />
      </Suspense>
    ),
    children: toApiRouter(),
  },
]);

export default root;
