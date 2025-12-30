import subprocess

from rich.progress import track

from os import makedirs
from os.path import exists, splitext
from shutil import rmtree

from glob import iglob
from pathlib import Path

subsets = {
	"latin": "U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+0304, U+0308, U+0329, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD",
	"latin-ext": "U+0100-02AF, U+0304, U+0308, U+0329, U+1E00-1E9F, U+1EF2-1EFF, U+2020, U+20A0-20AB, U+20AD-20C0, U+2113, U+2C60-2C7F, U+A720-A7FF",
	"greek": "U+0370-0377, U+037A-037F, U+0384-038A, U+038C, U+038E-03A1, U+03A3-03FF, U+1F00-1FFF",
}
output_dir = "output"
layout_features = "ccmp,locl,mark,mkmk,calt,dlig,case,cpsp,kern,ss03,cv05,cv06"

if exists(output_dir):
	rmtree(output_dir)

makedirs(output_dir)

for filepath in iglob("input/*.woff2"):
	filename = Path(filepath).name

	for subset in track(subsets, description=f"Subsetting {filename}..."):
		u_range = subsets[subset]
		outputfile = splitext(filename)[0]

		subp_args = [
			"pyftsubset",
			filepath,
			f"--unicodes={u_range}",
			"--flavor=woff2",
			f"--output-file={output_dir}/{outputfile}.{subset}.woff2",
		]

		if layout_features is not None:
			subp_args.append(f"--layout-features+={layout_features}")

		subprocess.run(subp_args, check=True)
