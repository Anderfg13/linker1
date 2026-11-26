const form = document.getElementById('link-form');
const urlInput = document.getElementById('url');
const aliasInput = document.getElementById('alias');
const message = document.getElementById('message');
const result = document.getElementById('result');
const shortLink = document.getElementById('short-link');
const copyButton = document.getElementById('copy-btn');
const deleteButton = document.getElementById('delete-btn');
const statsButton = document.getElementById('stats-btn');
const statsResult = document.getElementById('stats-result');

let currentLinkId = null;

function setMessage(text, isError = false) {
  message.textContent = text;
  message.style.color = isError ? '#fca5a5' : '#9fb2d1';
}

function setResult(shortUrl, linkId) {
  shortLink.textContent = shortUrl;
  shortLink.href = shortUrl;
  currentLinkId = linkId;
  result.classList.remove('hidden');
  statsResult.textContent = '';
}

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  result.classList.add('hidden');
  statsResult.textContent = '';
  setMessage('Creating link...');

  try {
    const alias = aliasInput.value.trim();
    const payload = { url: urlInput.value.trim() };
    if (alias) {
      payload.alias = alias;
    }

    const response = await fetch('/link', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

    const text = await response.text();

    if (!response.ok) {
      throw new Error(text || 'Could not shorten the URL.');
    }

    const shortPath = response.headers.get('Location') || `/${text}`;
    const shortUrl = new URL(shortPath, window.location.origin).toString();
    const linkId = shortPath.startsWith('/') ? shortPath.slice(1) : shortPath;

    setResult(shortUrl, linkId);
    setMessage('Link ready.');
  } catch (error) {
    setMessage(error.message, true);
  }
});

copyButton.addEventListener('click', async () => {
  const value = shortLink.href;
  if (!value) {
    return;
  }

  try {
    await navigator.clipboard.writeText(value);
    setMessage('Link copied to clipboard.');
  } catch (error) {
    setMessage('Could not copy the link.', true);
  }
});

deleteButton.addEventListener('click', async () => {
  if (!currentLinkId) {
    return;
  }

  setMessage('Deleting link...');

  try {
    const response = await fetch(`/${currentLinkId}`, { method: 'DELETE' });
    statsResult.textContent = '';

    if (!response.ok && response.status !== 404) {
      throw new Error('Could not delete the link.');
    }

    result.classList.add('hidden');
    currentLinkId = null;
    setMessage('Link deleted.');
  } catch (error) {
    setMessage(error.message, true);
  }
});

statsButton.addEventListener('click', async () => {
  if (!currentLinkId) {
    return;
  }

  statsResult.textContent = 'Consultando aperturas...';

  try {
    const response = await fetch(`/link/${encodeURIComponent(currentLinkId)}/stats`);

    if (response.status === 404) {
      statsResult.textContent = 'Este enlace ya no existe, así que no hay aperturas para mostrar.';
      return;
    }
    if (!response.ok) {
      throw new Error('stats');
    }

    const data = await response.json();
    statsResult.textContent = data.clicks === 1
        ? 'Tu enlace se ha abierto 1 vez.'
        : `Tu enlace se ha abierto ${data.clicks} veces.`;
  } catch (error) {
    statsResult.textContent = 'No pudimos consultar las aperturas. Inténtalo de nuevo.';
  }
});