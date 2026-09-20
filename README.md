# Sies2Csv


Esta herramienta es únicamente de utilidad para profesores de la Universidad de Oviedo y que utilicen [Classroom 50](https://github.com/foundation50/classroom50/wiki).

Concretamente sirve para convertir el fichero Excel que contiene la lista de alumnos de una asignatura y que es generado por SIES (el sistema de información de la Universidad de Oviedo) al formato CSV que requiere la herramienta [roster50.jar](https://github.com/raul-izquierdo/roster50) para actualizar la lista de estudiantes de Classroom 50.

Para ver cómo encaja esta herramienta en el proceso, leer la documentación de [roster50](https://github.com/raul-izquierdo/roster50), ya que _sies2csv_ es un paso previo a la ejecución de _roster50_ para el caso concreto de la Universidad de Oviedo.

## Installation

Download [sies2csv.jar](https://github.com/raul-izquierdo/sies2csv/releases/latest/download/sies2csv.jar) from the latest release of this repository.

To verify that it works, run the _version_ or _help_ command:

```bash
java -jar sies2csv.jar -V
```

```bash
java -jar sies2csv.jar -h
```


## Excuting the tool

<!-- TODO: 📅 /**/  -->


## Command line Arguments


Syntax:

```bash
java -jar sies2csv.jar [flags] [sies-file]
```

**sies-file** is the name of the excel file downloaded from SIES. Can be omitted if the file is named `alumnosMatriculados.xls`.

Flags:
- **-z [output-roster.csv]**: The name of the generated CSV file. If not provided, defaults to `new-roster.csv` (the name expected by _roster50.jar_).
- **-g [groups.csv]**: The file listing the groups to be considered for the teacher. Groups not present in this file will not be considered. If not provided, defaults to `groups.csv`.
- **-h, --help**: Show help.
- **-V, --version**: Show version.

## Exit Codes

The exit codes indicate the result of the command execution:

- **0**: Changes were found.
- **1**: No changes found. The roster is up to date.
- **2**: An error occurred.

## License

See `LICENSE`.
Copyright (c) 2025 Raul Izquierdo Castanedo
