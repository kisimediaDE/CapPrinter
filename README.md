<h1 align="center">Cap Printer Plugin</h1><br>
<p align="center"><strong><code>kisimediaDE/cap-printer</code></strong></p>
<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2025?style=for-the-badge" />
  <a href="https://www.npmjs.com/package/cap-printer"><img src="https://img.shields.io/npm/dw/cap-printer?style=for-the-badge" /></a>
</p>
<p align="center">

Capacitor plugin for printing PDF documents - both local and remote.

</p>

## Installation

```bash
npm install cap-printer
npx cap sync
```

## API

<docgen-index>

- [Installation](#installation)
- [API](#api)
  - [print(...)](#print)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### print(...)

```typescript
print(options: { url?: string; localPath?: string; }) => Promise<void>
```

| Param         | Type                                               |
| ------------- | -------------------------------------------------- |
| **`options`** | <code>{ url?: string; localPath?: string; }</code> |

---

</docgen-api>

## Usage

In your JavaScript/TypeScript code, you can use the plugin as follows:

```typescript
import { CapPrinter } from 'cap-printer';

async function printPDF() {
  try {
    // To print a remote PDF:
    await CapPrinter.print({ url: 'https://example.com/your.pdf' });

    // Or to print a local file:
    // await CapPrinter.print({ localPath: '/path/to/your/file.pdf' });

    console.log('Print process started successfully.');
  } catch (error) {
    console.error('Print process failed:', error);
  }
}
```

## Contributing

Contributions are welcome!

## License

This plugin is licensed under the MIT License.
