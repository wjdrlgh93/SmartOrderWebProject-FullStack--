import { lazy, Suspense } from "react";

const Loading = <div className="loading">Loading...</div>;

const NoticeListPage = lazy(() =>
  import("../components/container/notice/NoticeListContainer")
);

const NoticeDetailPage = lazy(() =>
  import("../components/container/notice/NoticeDetailContainer")
);

export const toNoticeRouter = () => {
  return [
    {
      path: "",
      element: (
        <Suspense fallback={Loading}>
          <NoticeListPage />
        </Suspense>
      ),
    },
    {
      path: "detail/:noticeId",
      element: (
        <Suspense fallback={Loading}>
          <NoticeDetailPage />
        </Suspense>
      ),
    },
  ];
};
