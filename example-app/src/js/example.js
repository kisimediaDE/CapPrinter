import { CapPrinter } from 'cap-printer';

const printButton = document.getElementById('printButton');
const statusDiv = document.getElementById('status');

printButton.addEventListener('click', async () => {
  const urlInput = document.getElementById('pdfUrl');
  const localInput = document.getElementById('localPath');
  const pdfUrl = urlInput.value.trim();
  const localPath = localInput.value.trim();

  if (!pdfUrl && !localPath) {
    statusDiv.textContent = 'Please enter either a PDF URL or a local file path.';
    statusDiv.style.color = 'red';
    return;
  }

  try {
    await CapPrinter.print({ url: pdfUrl, localPath: localPath });
    statusDiv.textContent = 'Print process started successfully.';
    statusDiv.style.color = 'green';
  } catch (error) {
    statusDiv.textContent = `Printing failed: ${error.message || error}`;
    statusDiv.style.color = 'red';
  }
});
