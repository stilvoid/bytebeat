# Bytebeat

A collection of bytebeat tools and tunes.

## Usage

```
Usage: bb [OPTION...] EXPRESSION

Plays the bytebeat EXPRESSION as audio.

Options:
  -h,--help           Display this message
  -l,--length LENGTH  Play audio for LENGTH seconds (default 16)
  -o,--output FILE    Output autio to FILE as 8-bit WAV
```

## Examples

Some example expressions and audio files generated from them are included in the [examples](./examples/) folder.

You can play the example files yourself like this: `bb $(<examples/1)`, replacing `1` with whichever example number you want to hear.

Here are some links to the audio files:

* [`(t|t>>4|t>>6|t<<2)+(t>>5|t>>4)&(t>>8|t>>7)&(t<<1)^(t>>6)&(t/32)&(t<<99)*t>>15^(t>>8)`](./examples/1.wav?raw=true)

* [`t*2*(4000/(t%4000+1))|(t<<1|t>>4|t>>7)|(t*t*(t%4000>2000))|(t>>10|t<<3)&t>>6`](./examples/2.wav?raw=true)

* [`(t<<0)*(t%2000<1000)|(t<<1)*(t%1000<500)|(t<<2)*(t%3000<1000)`](./examples/3.wav?raw=true)

* [`(t<<1|t>>2)^(t*(10-t/100%10)*(t%4000<1000))|(t*t*(t%8000>4000)*(t%8000<5000))`](./examples/4.wav?raw=true)
