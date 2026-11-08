"use client";

import StatusPage from "./_components/StatusPage/statusPage";

export default function ErrorPage({
  error: _error,
  reset: _reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  return (
    <StatusPage
      code="500"
      label="Server Error"
      message="エラーが発生しました"
    />
  );
}
