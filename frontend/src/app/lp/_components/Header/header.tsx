'use client'
import './header.css';

export default function Header() {
  const clickLogo = () => {
    window.alert('ロゴが押されました')
  }
  return (
    <header className="lp-header">
      <nav className="lp-nav">
        <div className="lp-nav-content">
          <div>
            <a className="lp-logo" onClick={clickLogo}>
              {/* FIXME 仮置き */}
              <img width={80} height={40} style={{ background: 'yellow' }} />
            </a>
          </div>
          <div>
            <a onClick={clickLogo}>
              <p>豆を購入する</p>
            </a>
          </div>
        </div>
      </nav>
    </header>
  );
}
