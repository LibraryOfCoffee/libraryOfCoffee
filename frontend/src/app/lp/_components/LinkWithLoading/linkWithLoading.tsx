"use client";

import Link from "next/link";
import type { ComponentProps } from "react";
import { useEffect, useState } from "react";
import LoadingOverlay from "../LoadingOverlay/loadingOverlay";

type LinkWithLoadingProps = ComponentProps<typeof Link>;

export default function LinkWithLoading(props: LinkWithLoadingProps) {
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const handlePageShow = (e: PageTransitionEvent) => {
      if (e.persisted) {
        setLoading(false);
      }
    };
    window.addEventListener("pageshow", handlePageShow);
    return () => window.removeEventListener("pageshow", handlePageShow);
  }, []);

  return (
    <>
      {loading && <LoadingOverlay />}
      <Link {...props} onClick={() => setLoading(true)} />
    </>
  );
}
