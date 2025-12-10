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


const IndexPage = lazy(() => import(`../pages/IndexPage`));

const root = createBrowserRouter([
  {

    path: "",
    element: (
      <Suspense fallback={Loading}>
        <IndexPage />
      </Suspense>
    ),
  },
  {

    path: "admin",
    element: (
      <Suspense fallback={Loading}>
        <AdminLayout />
      </Suspense>
    ),
    children: toAdminRouter(),
  },
  {

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

    path: "store",
    element: (
      <Suspense fallback={Loading}>
        <StoreLayout />
      </Suspense>
    ),
    children: toStoreRouter(),
  },
  {

    path: "crew",
    element: (
      <Suspense fallback={Loading}>
        <CrewLayout />
      </Suspense>
    ),
    children: toCrewRouter(),
  },
  {

    path: "board",
    element: (
      <Suspense fallback={Loading}>
        <BoardLayout />
      </Suspense>
    ),
    children: toBoardRouter(),
  },
  {

    path: "notice",
    element: (
      <Suspense fallback={Loading}>
        <NoticeLayout />
      </Suspense>
    ),
    children: toNoticeRouter(),
  },
  {

    path: "cart",
    element: (
      <Suspense fallback={Loading}>
        <CartLayout />
      </Suspense>
    ),
    children: toCartRouter(),
  },
  {

    path: "payment",
    element: (
      <Suspense fallback={Loading}>
        <PaymentLayout />
      </Suspense>
    ),
    children: toPaymentRouter(),
  },
  {

    path: "mycrew/:crewId",
    element: (
      <Suspense fallback={Loading}>
        <MyCrewLayout />
      </Suspense>
    ),
    children: toMyCrewRouter(),
  },
  {

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
