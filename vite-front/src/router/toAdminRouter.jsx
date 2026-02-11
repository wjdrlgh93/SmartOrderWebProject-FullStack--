import React, { lazy, Suspense } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div className="loading">Loading...</div>;

const AdminIndex = lazy(() =>
  import("../components/container/admin/AdminIndexContainer")
);

const AdminMember = lazy(() =>
  import("../components/container/admin/member/AdminMemberListContainer")
);
const AdminMemberDetail = lazy(() =>
  import("../components/container/admin/member/AdminMemberDetailContainer")
);

const AdminCrew = lazy(() =>
  import("../components/container/admin/crew/AdminCrewListContainer")
);
const AdminCrewAllow = lazy(() =>
  import("../components/container/admin/crew/AdminCrewAllowContainer")
);
const AdminCrewDetail = lazy(() =>
  import("../components/container/admin/crew/AdminCrewDetailContainer")
);

const AdminPayment = lazy(() =>
  import("../components/container/admin/payment/AdminPaymentListContainer")
);
const AdminPaymentDetail = lazy(() =>
  import("../components/container/admin/payment/AdminPaymentDetailContainer")
);

const AdminAddItem = lazy(() =>
  import("../components/container/admin/item/AdminAddItemContainer")
);
const AdminItem = lazy(() =>
  import("../components/container/admin/item/AdminItemListContainer")
);
const AdminItemDetail = lazy(() =>
  import("../components/container/admin/item/AdminItemDetailContainer")
);

const AdminBoard = lazy(() =>
  import("../components/container/admin/board/AdminBoardListContainer")
);
const AdminBoardDetail = lazy(() =>
  import("../components/container/admin/board/AdminBoardDetailContainer")
);

const AdminAddNotice = lazy(() =>
  import("../components/container/admin/notice/AdminAddNoticeContainer")
);
const AdminNoticeList = lazy(() =>
  import("../components/container/admin/notice/AdminNoticeListContainer")
);
const AdminNoticeDetail = lazy(() =>
  import("../components/container/admin/notice/AdminNoticeDetailContainer")
);

const AdminNoticeUpdate = lazy(() =>
  import("../components/container/admin/notice/AdminNoticeUpdateContainer")
);

const toAdminRouter = () => {
  return [
    {
      path: "",
      element: <Navigate replace to={"index"} />,
    },
    {
      path: "index",
      element: (
        <Suspense fallback={Loading}>
          <AdminIndex />
        </Suspense>
      ),
    },

    // Member
    {
      path: "memberList",
      element: (
        <Suspense fallback={Loading}>
          <AdminMember />
        </Suspense>
      ),
    },
    {
      path: "memberDetail/:memberId",
      element: (
        <Suspense fallback={Loading}>
          <AdminMemberDetail />
        </Suspense>
      ),
    },

    // Crew
    {
      path: "crewlist",
      element: (
        <Suspense fallback={Loading}>
          <AdminCrew />
        </Suspense>
      ),
    },
    {
      path: "crewAllow",
      element: (
        <Suspense fallback={Loading}>
          <AdminCrewAllow />
        </Suspense>
      ),
    },
    {
      path: "crewDetail/:crewId",
      element: (
        <Suspense fallback={Loading}>
          <AdminCrewDetail />
        </Suspense>
      ),
    },

    // Payment
    {
      path: "paymentlist",
      element: (
        <Suspense fallback={Loading}>
          <AdminPayment />
        </Suspense>
      ),
    },
    {
      path: "paymentDetail/:paymentId",
      element: (
        <Suspense fallback={Loading}>
          <AdminPaymentDetail />
        </Suspense>
      ),
    },

    // Item
    {
      path: "addItem",
      element: (
        <Suspense fallback={Loading}>
          <AdminAddItem />
        </Suspense>
      ),
    },
    {
      path: "itemlist",
      element: (
        <Suspense fallback={Loading}>
          <AdminItem />
        </Suspense>
      ),
    },
    {
      path: "itemDetail/:itemId",
      element: (
        <Suspense fallback={Loading}>
          <AdminItemDetail />
        </Suspense>
      ),
    },

    // Board
    {
      path: "boardlist",
      element: (
        <Suspense fallback={Loading}>
          <AdminBoard />
        </Suspense>
      ),
    },
    {
      path: "boardDetail/:boardId",
      element: (
        <Suspense fallback={Loading}>
          <AdminBoardDetail />
        </Suspense>
      ),
    },

    // Event
    {
      path: "addNotice",
      element: (
        <Suspense fallback={Loading}>
          <AdminAddNotice />
        </Suspense>
      ),
    },
    {
      path: "noticeList",
      element: (
        <Suspense fallback={Loading}>
          <AdminNoticeList />
        </Suspense>
      ),
    },
    {
      path: "notice/detail/:noticeId",
      element: (
        <Suspense fallback={Loading}>
          <AdminNoticeDetail />
        </Suspense>
      ),
    },
    {
      path: "notice/update/:noticeId",
      element: (
        <Suspense fallback={Loading}>
          <AdminNoticeUpdate />
        </Suspense>
      ),
    },
  ];
};

export default toAdminRouter;
