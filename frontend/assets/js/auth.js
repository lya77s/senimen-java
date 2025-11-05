function getToken(){ return localStorage.getItem('token'); }
function getUser(){ try{return JSON.parse(localStorage.getItem('user')||'null');}catch{return null;} }
function isLoggedIn(){ return !!getToken() && !!getUser(); }
function hasRole(role){ const u=getUser(); return u && (u.role||'').toLowerCase()===role.toLowerCase(); }
function requireAuth(){ if(!isLoggedIn()) location.href='/frontend/login.html'; }
function requireRole(...roles){ requireAuth(); const u=getUser(); if(!u) return location.href='/frontend/login.html'; const ur=(u.role||'').toLowerCase(); if(!roles.map(r=>r.toLowerCase()).includes(ur)) location.href='/frontend/index.html'; }
function logout(){ localStorage.removeItem('token'); localStorage.removeItem('user'); location.href='/frontend/index.html'; }

function renderNavbar(){
  const u = getUser();
  const authLinks = u? `
    <a class="hover:text-blue-700 text-base" href="/frontend/profile.html">${u.name}</a>
    ${hasRole('organizer')?'<a class="hover:text-blue-700 text-base" href="/frontend/org-dashboard.html">Organizer</a>':''}
    ${hasRole('admin')?'<a class="hover:text-blue-700 text-base" href="/frontend/admin-dashboard.html">Admin</a>':''}
    <button id="logoutBtn" class="ml-3 bg-gray-900 text-white rounded px-4 py-2 text-base hover:bg-gray-800">Logout</button>
  `: `
    <a class="hover:text-blue-700 text-base" href="/frontend/register.html">Register</a>
    <a class="hover:text-blue-700 text-base" href="/frontend/login.html">Login</a>
  `;
  const html = `
  <header class="sticky top-0 z-50 bg-white/80 backdrop-blur border-b">
    <div class="container mx-auto px-4 py-5 flex items-center justify-between">
      <a class="flex items-center space-x-2" href="/frontend/index.html">
        <img src="/frontend/assets/img/brand-logo.png" alt="Senimen" class="h-12 w-auto" onerror="this.onerror=null;this.src='/frontend/assets/img/logo.svg'"/>
        <span class="text-3xl font-bold text-slate-900">Senimen</span>
      </a>
      <nav class="space-x-6 text-slate-700">
        <a class="hover:text-blue-700" href="/frontend/events.html">Events</a>
        <a class="hover:text-blue-700" href="/frontend/about.html">About</a>
        <a class="hover:text-blue-700" href="/frontend/sponsors.html">Sponsors</a>
        ${authLinks}
      </nav>
    </div>
  </header>`;
  const c = document.createElement('div'); c.innerHTML = html; document.body.prepend(c);
  const lb = document.getElementById('logoutBtn'); if(lb) lb.onclick=logout;
}

function renderFooter(){
  const html = `
  <footer class="mt-12 border-t">
    <div class="container mx-auto px-4 py-8 text-sm text-slate-600 flex flex-col md:flex-row md:items-center md:justify-between">
      <div class="flex items-center space-x-2">
        <img src="/frontend/assets/img/logo.svg" alt="Senimen" class="h-6 w-auto"/>
        <span>© ${new Date().getFullYear()} Senimen</span>
      </div>
      <div class="space-x-4">
        <a class="hover:text-blue-700" href="/frontend/index.html">Home</a>
        <a class="hover:text-blue-700" href="#">About</a>
        <a class="hover:text-blue-700" href="#">Sponsors</a>
      </div>
    </div>
  </footer>`;
  const c = document.createElement('div'); c.innerHTML = html; document.body.appendChild(c);
}
