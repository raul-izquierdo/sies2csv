# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).



## [1.0.0]


### Added

- Validates that the file is a valid SIES file by checking its headers.

- Extracts each student's name, email address, and group into the `first_name`, `email`, and `section` fields.
    - When a group is provided:
        - Removes the prefix, retaining only the number.
        - Converts the English-group prefix to a single `i`.
- Reports data-quality issues in the Excel file, including students who are missing a name, email address, or group.
- Filters records as follows:
    - Excludes:
        - Students who are not in the specified groups.
        - Students without an email address, since their ID is required.
    - Retains:
        - Students without a name; Classroom 50 will use their email address as their name.
        - Students without an assigned group, allowing them to receive assignments until a group is assigned. A group is only required to view solutions.
- Generates output in which:
    - The email address is always present because records without one have been filtered out.
    - Missing names and groups are represented by empty strings.
    - Quotes are added to names to allow for commas in names.
