"use client";

import Link from "next/link";
import type { ComponentProps } from "react";
import { useState } from "react";
import LoadingOverlay from "../LoadingOverlay/loadingOverlay";

type LinkWithLoadingProps = ComponentProps<typeof Link>;

export default function LinkWithLoading(props: LinkWithLoadingProps) {
  const [loading, setLoading] = useState(false);

  return (
    <>
      {loading && <LoadingOverlay />}
      <Link {...props} onClick={() => setLoading(true)} />
    </>
  );
}
