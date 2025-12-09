import React, { lazy, Suspense } from "react";

const Loading = <div>Loading...</div>;

// API Pages
const MarathonApiPage = lazy(() => import("../components/container/api/Marathon_list"));

const toApiRouter = () => [
  {
    path: "marathon",
    element: (
      <Suspense fallback={Loading}>
        <MarathonApiPage />
      </Suspense>
    ),
  },
];

export default toApiRouter;
