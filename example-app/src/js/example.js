import { CapPrinter } from 'cap-printer';

const printButton = document.getElementById('printButton');
const statusDiv = document.getElementById('status');

printButton.addEventListener('click', async () => {
  const urlInput = document.getElementById('pdfUrl');
  const localInput = document.getElementById('localPath');
  const outputType = document.getElementById('outputType').value;
  const orientation = document.getElementById('orientation').value;
  const duplex = document.getElementById('duplex').checked;

  const url = urlInput.value.trim();
  const localPath = localInput.value.trim();

  if (!url && !localPath) {
    statusDiv.textContent = 'Please enter either a PDF URL or a local file path.';
    statusDiv.style.color = 'red';
    return;
  }


  const { available } = await CapPrinter.isAvailable();

  if (!available) {
    console.warn('CapPrinter is not available on this platform.');
    statusDiv.textContent = 'CapPrinter is not available on this platform.';
    statusDiv.style.color = 'orange';
    return;
  }

  try {
    await CapPrinter.print({
      url,
      localPath,
      options: {
        outputType,
        orientation,
        duplex
      }
    });
    statusDiv.textContent = 'Print process started successfully.';
    statusDiv.style.color = 'green';
  } catch (error) {
    statusDiv.textContent = `Printing failed: ${error.message || error}`;
    statusDiv.style.color = 'red';
  }
});
