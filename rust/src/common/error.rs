/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

use std::{collections::HashSet, error::Error as StdError, fmt};

use itertools::Itertools;
use tonic::{Code, Status};
use tonic_types::StatusExt;
use typeql::error_messages;

use super::{address::Address, RequestID};

error_messages! { ConnectionError
    code: "CXN", type: "Connection Error",
    RPCMethodUnavailable { message: String } =
        1: "The server does not support this method, please check the driver-server compatibility:\n'{message}'.",
    ConnectionOpenError =
        100: "Error opening connection.",
    ConnectionIsClosed =
        2: "The connection has been closed and no further operation is allowed.",
    TransactionIsClosed =
        3: "The transaction is closed and no further operation is allowed.",
    TransactionIsClosedWithErrors { errors: String } =
        4: "The transaction is closed because of the error(s):\n{errors}",
    DatabaseDoesNotExist { name: String } =
        5: "The database '{name}' does not exist.",
    MissingResponseField { field: &'static str } =
        6: "Missing field in message received from server: '{field}'. This is either a version compatibility issue or a bug.",
    UnknownRequestId { request_id: RequestID } =
        7: "Received a response with unknown request id '{request_id}'",
    QueryStreamNoResponse =
        101: "Didn't receive any server responses for the query.",
    InvalidResponseField { name: &'static str } =
        8: "Invalid field in message received from server: '{name}'. This is either a version compatibility issue or a bug.",
    UnexpectedResponse { response: String } =
        9: "Received unexpected response from server: '{response}'. This is either a version compatibility issue or a bug.",
    ServerConnectionFailed { addresses: Vec<Address> } =
        10: "Unable to connect to TypeDB server(s) at: \n{addresses:?}",
    ServerConnectionFailedWithError { error: String } =
        11: "Unable to connect to TypeDB server(s), received errors: \n{error}",
    ServerConnectionFailedStatusError { error: String } =
        12: "Unable to connect to TypeDB server(s), received network or protocol error: \n{error}",
    UserManagementCloudOnly =
        13: "User management is only available in TypeDB Cloud servers.",
    CloudReplicaNotPrimary =
        14: "The replica is not the primary replica.",
    CloudAllNodesFailed { errors: String } =
        15: "Attempted connecting to all TypeDB Cloud servers, but the following errors occurred: \n{errors}.",
    CloudTokenCredentialInvalid =
        16: "Invalid token credential.",
    SessionCloseFailed =
        17: "Failed to close session. It may still be open on the server: or it may already have been closed previously.",
    CloudEncryptionSettingsMismatch =
        18: "Unable to connect to TypeDB Cloud: possible encryption settings mismatch.",
    CloudSSLCertificateNotValidated =
        19: "SSL handshake with TypeDB Cloud failed: the server's identity could not be verified. Possible CA mismatch.",
    BrokenPipe =
        20: "Stream closed because of a broken pipe. This could happen if you are attempting to connect to an unencrypted cloud instance using a TLS-enabled credential.",
    ConnectionFailed =
        21: "Connection failed. Please check the server is running and the address is accessible. Encrypted Cloud endpoints may also have misconfigured SSL certificates.",
    MissingPort { address: String } =
        22: "Invalid URL '{address}': missing port.",
    AddressTranslationMismatch { unknown: HashSet<Address>, unmapped: HashSet<Address> } =
        23: "Address translation map does not match the server's advertised address list. User-provided servers not in the advertised list: {unknown:?}. Advertised servers not mapped by user: {unmapped:?}.",

    ValueTimeZoneNameNotRecognised { time_zone: String } =
        24: "Time zone provided by the server has name '{time_zone}', which is not an officially recognized timezone.",
    ValueTimeZoneOffsetNotImplemented { offset: i32 } =
        25: "Time zone provided by the server has numerical offset '{offset}', which is not yet supported by the driver.",
    ValueStructNotImplemented =
        26: "Struct valued responses are not yet supported by the driver.",
    ListsNotImplemented =
        27: "Lists are not yet supported by the driver."
}

error_messages! { InternalError
    code: "INT", type: "Internal Error",
    RecvError =
        1: "Channel is closed.",
    SendError =
        2: "Unable to send response over callback channel (receiver dropped).",
    UnexpectedRequestType { request_type: String } =
        3: "Unexpected request type for remote procedure call: {request_type}. This is either a version compatibility issue or a bug.",
    UnexpectedResponseType { response_type: String } =
        4: "Unexpected response type for remote procedure call: {response_type}. This is either a version compatibility issue or a bug.",
    UnknownServer { server: Address } =
        5: "Received replica at unrecognized server: {server}.",
    EnumOutOfBounds { value: i32, enum_name: &'static str } =
        6: "Value '{value}' is out of bounds for enum '{enum_name}'.",
}

#[derive(Clone, PartialEq, Eq)]
pub struct ServerError {
    error_code: String,
    error_domain: String,
    message: String,
    stack_trace: Vec<String>,
}

impl ServerError {
    pub(crate) fn new(error_code: String, error_domain: String, message: String, stack_trace: Vec<String>) -> Self {
        Self { error_code, error_domain, message, stack_trace }
    }

    pub(crate) fn format_code(&self) -> &str {
        &self.error_code
    }

    pub(crate) fn message(&self) -> &str {
        &self.message
    }
}

impl fmt::Display for ServerError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        if self.stack_trace.is_empty() {
            write!( f, "[{}] {}. {}", self.error_code, self.error_domain, self.message)
        } else {
            write!( f, "{}", format!("\n{}", self.stack_trace.join("\nCaused: ")))
        }
    }
}

impl fmt::Debug for ServerError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> fmt::Result {
        fmt::Display::fmt(self, f)
    }
}

/// Represents errors encountered during operation.
#[derive(Clone, Debug, PartialEq, Eq)]
pub enum Error {
    Connection(ConnectionError),
    Internal(InternalError),
    Server(ServerError),
    TypeQL(typeql::Error),
    Other(String),
}

impl Error {
    pub fn code(&self) -> String {
        match self {
            Self::Connection(error) => error.format_code(),
            Self::Internal(error) => error.format_code(),
            Self::Server(error) => error.format_code().to_owned(),
            Self::TypeQL(_error) => String::new(),
            Self::Other(_error) => String::new(),
        }
    }

    pub fn message(&self) -> String {
        match self {
            Self::Connection(error) => error.message(),
            Self::Internal(error) => error.message(),
            Self::Server(error) => error.message().to_owned(),
            Self::TypeQL(error) => error.to_string(),
            Self::Other(error) => error.clone(),
        }
    }

    fn from_message(message: &str) -> Self {
        match message.split_ascii_whitespace().next() {
            Some("[RPL01]") => Self::Connection(ConnectionError::CloudReplicaNotPrimary),
            // TODO: CLS and ENT are synonyms which we can simplify on protocol change
            Some("[CLS08]") => Self::Connection(ConnectionError::CloudTokenCredentialInvalid),
            Some("[ENT08]") => Self::Connection(ConnectionError::CloudTokenCredentialInvalid),
            Some("[DBS06]") => Self::Connection(ConnectionError::DatabaseDoesNotExist {
                name: message.split('\'').nth(1).unwrap_or("{unknown}").to_owned(),
            }),
            _ => Self::Other(message.to_owned()),
        }
    }

    fn parse_unavailable(status_message: &str) -> Error {
        if status_message == "broken pipe" {
            Error::Connection(ConnectionError::BrokenPipe)
        } else if status_message.contains("received corrupt message") {
            Error::Connection(ConnectionError::CloudEncryptionSettingsMismatch)
        } else if status_message.contains("UnknownIssuer") {
            Error::Connection(ConnectionError::CloudSSLCertificateNotValidated)
        } else if status_message.contains("Connection refused") {
            Error::Connection(ConnectionError::ConnectionFailed)
        } else {
            Error::Connection(ConnectionError::ServerConnectionFailedStatusError { error: status_message.to_owned() })
        }
    }
}

impl fmt::Display for Error {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::Connection(error) => write!(f, "{error}"),
            Self::Internal(error) => write!(f, "{error}"),
            Self::Server(error) => write!(f, "{error}"),
            Self::TypeQL(error) => write!(f, "{error}"),
            Self::Other(message) => write!(f, "{message}"),
        }
    }
}

impl StdError for Error {
    fn source(&self) -> Option<&(dyn StdError + 'static)> {
        match self {
            Self::Connection(error) => Some(error),
            Self::Internal(error) => Some(error),
            Self::TypeQL(error) => Some(error),
            Self::Server(_) => None,
            Self::Other(_) => None,
        }
    }
}

impl From<ConnectionError> for Error {
    fn from(error: ConnectionError) -> Self {
        Self::Connection(error)
    }
}

impl From<InternalError> for Error {
    fn from(error: InternalError) -> Self {
        Self::Internal(error)
    }
}

impl From<ServerError> for Error {
    fn from(error: ServerError) -> Self {
        Self::Server(error)

    }
}

impl From<typeql::Error> for Error {
    fn from(err: typeql::Error) -> Self {
        Self::TypeQL(err)
    }
}

impl From<Status> for Error {
    fn from(status: Status) -> Self {
        if let Ok(details) = status.check_error_details() {
            if let Some(bad_request) = details.bad_request() {
                Self::Connection(ConnectionError::ServerConnectionFailedWithError { error: format!("{:?}", bad_request) })
            } else if let Some(error_info) = details.error_info() {
                let code = error_info.reason.clone();
                let domain = error_info.domain.clone();
                let stack_trace = if let Some(debug_info) = details.debug_info() {
                    debug_info.stack_entries.clone()
                } else {
                    vec![]
                };
                Self::Server(ServerError::new(code, domain, status.message().to_owned(), stack_trace))
            } else {
                Self::from_message(status.message())
            }
        } else {
            if status.code() == Code::Unavailable {
                Self::parse_unavailable(status.message())
            } else if status.code() == Code::Unknown
                || is_rst_stream(&status)
                || status.code() == Code::InvalidArgument
                || status.code() == Code::FailedPrecondition
                || status.code() == Code::AlreadyExists
            {
                Self::Connection(ConnectionError::ServerConnectionFailedStatusError { error: status.message().to_owned() })
            } else if status.code() == Code::Unimplemented {
                Self::Connection(ConnectionError::RPCMethodUnavailable { message: status.message().to_owned() })
            } else {
                Self::from_message(status.message())
            }
        }
    }
}

fn is_rst_stream(status: &Status) -> bool {
    // "Received Rst Stream" occurs if the server is in the process of shutting down.
    status.message().contains("Received Rst Stream")
}

impl From<http::uri::InvalidUri> for Error {
    fn from(err: http::uri::InvalidUri) -> Self {
        Self::Other(err.to_string())
    }
}

impl From<tonic::transport::Error> for Error {
    fn from(err: tonic::transport::Error) -> Self {
        Self::Other(err.to_string())
    }
}

impl<T> From<tokio::sync::mpsc::error::SendError<T>> for Error {
    fn from(_err: tokio::sync::mpsc::error::SendError<T>) -> Self {
        Self::Internal(InternalError::SendError)
    }
}

impl From<tokio::sync::oneshot::error::RecvError> for Error {
    fn from(_err: tokio::sync::oneshot::error::RecvError) -> Self {
        Self::Internal(InternalError::RecvError)
    }
}

impl From<crossbeam::channel::RecvError> for Error {
    fn from(_err: crossbeam::channel::RecvError) -> Self {
        Self::Internal(InternalError::RecvError)
    }
}

impl<T> From<crossbeam::channel::SendError<T>> for Error {
    fn from(_err: crossbeam::channel::SendError<T>) -> Self {
        Self::Internal(InternalError::SendError)
    }
}

impl From<String> for Error {
    fn from(err: String) -> Self {
        Self::Other(err)
    }
}

impl From<std::io::Error> for Error {
    fn from(err: std::io::Error) -> Self {
        Self::Other(err.to_string())
    }
}
