<h1 align="center">Cap Printer Plugin</h1><br>
<p align="center"><strong><code>kisimediaDE/cap-printer</code></strong></p>
<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2025?style=for-the-badge" />
  <a href="https://www.npmjs.com/package/cap-printer"><img src="https://img.shields.io/npm/dw/cap-printer?style=for-the-badge" /></a>
</p>
<p align="center">

Capacitor plugin for printing PDF documents - both local and remote.

</p>

## 📦 Installation

```bash
npm install cap-printer
npx cap sync
```

## 🔌 API

- [📦 Installation](#-installation)
- [🔌 API](#-api)
  - [print(...)](#print)
  - [isAvailable()](#isavailable)
  - [Interfaces](#interfaces)
    - [PrintRequest](#printrequest)
    - [PrintOptions](#printoptions)
- [🧪 Usage](#-usage)
  - [Basic usage](#basic-usage)
  - [For local files](#for-local-files)
  - [Plugin availability check (recommended for Web support)](#plugin-availability-check-recommended-for-web-support)
- [📝 Changelog](#-changelog)
- [🤝 Contributing](#-contributing)
- [🪪 License](#-license)

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

Plugin interface for CapPrinter.

### print(...)

```typescript
print(options: PrintRequest) => Promise<void>
```

Print a PDF document from a remote URL or local file path.

Either `url` or `localPath` must be provided.

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code><a href="#printrequest">PrintRequest</a></code> |

---

### isAvailable()

```typescript
isAvailable() => Promise<{ available: boolean; }>
```

Returns whether the plugin is available on the current platform.

**Returns:** <code>Promise&lt;{ available: boolean; }&gt;</code>

---

### Interfaces

#### PrintRequest

Parameters for a print request.

| Prop            | Type                                                  | Description                                                       |
| --------------- | ----------------------------------------------------- | ----------------------------------------------------------------- |
| **`url`**       | <code>string</code>                                   | Remote PDF file URL. Optional if `localPath` is provided.         |
| **`localPath`** | <code>string</code>                                   | Local file path to a PDF document. Optional if `url` is provided. |
| **`options`**   | <code><a href="#printoptions">PrintOptions</a></code> | Optional native print configuration.                              |

#### PrintOptions

Optional print configuration for native print dialogs.

| Prop              | Type                                             | Description                                                                                                                                                      |
| ----------------- | ------------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`outputType`**  | <code>'general' \| 'photo' \| 'grayscale'</code> | Defines the output quality or color mode. - `general`: Default system quality - `photo`: High-quality color/photo print - `grayscale`: Black & white / grayscale |
| **`orientation`** | <code>'portrait' \| 'landscape'</code>           | Sets the page orientation. - `portrait`: Standard vertical layout - `landscape`: Horizontal layout                                                               |
| **`duplex`**      | <code>boolean</code>                             | Enables duplex printing if the printer supports it.                                                                                                              |

</docgen-api>

## 🧪 Usage

### Basic usage

```typescript
import { CapPrinter } from 'cap-printer';

await CapPrinter.print({
  url: 'https://example.com/your.pdf',
  options: {
    outputType: 'photo',
    orientation: 'landscape',
    duplex: true,
  },
});
```

### For local files

```typescript
import { CapPrinter } from 'cap-printer';

await CapPrinter.print({
  localPath: '/path/to/your/file.pdf',
});
```

### Plugin availability check (recommended for Web support)

```typescript
import { CapPrinter } from 'cap-printer';

const { available } = await CapPrinter.isAvailable();

if (!available) {
  console.warn('CapPrinter is not available on this platform.');
  return;
}

await CapPrinter.print({ url: 'https://example.com/your.pdf' });
```

> ℹ️ On Web, `isAvailable()` always returns `false`.

## 📝 Changelog

For a detailed list of changes and version history, check out the [CHANGELOG.md](./CHANGELOG.md).

## 🤝 Contributing

Contributions are welcome!  
If you'd like to improve the plugin, report an issue, or suggest a feature, feel free to [open a pull request](https://github.com/kisimediaDE/CapPrinter/pulls) or [create an issue](https://github.com/kisimediaDE/CapPrinter/issues).

## 🪪 License

This plugin is licensed under the MIT License.
