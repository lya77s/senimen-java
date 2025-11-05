const apiBase = 'http://localhost:8080';
async function apiFetch(path, options={}){
  const token = localStorage.getItem('token');
  const headers = Object.assign({'Content-Type':'application/json'}, options.headers||{});
  if(token) headers['Authorization'] = 'Bearer '+token;
  const res = await fetch(apiBase+path, {...options, headers});
  if(!res.ok){ const err = new Error('HTTP '+res.status); err.status = res.status; try{err.body= await res.json();}catch{} throw err; }
  const ct = res.headers.get('content-type')||'';
  if(ct.includes('application/json')) return res.json();
  return res.text();
}
