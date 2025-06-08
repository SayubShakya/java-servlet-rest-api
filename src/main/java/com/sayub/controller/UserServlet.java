package com.sayub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayub.dto.request.CreateUserRequest;
import com.sayub.dto.request.UpdateUserRequest;
import com.sayub.dto.response.ServerResponse;
import com.sayub.dto.response.UserResponse;
import com.sayub.entity.User;
import com.sayub.exception.ApplicationException;
import com.sayub.repository.UserRepository;
import com.sayub.repository.impl.UserRepositoryImpl;
import com.sayub.service.UserService;
import com.sayub.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        UserRepository userRepository = new UserRepositoryImpl();
        this.userService = new UserServiceImpl(userRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<UserResponse> userResponses = userService.getAllUsers().stream()
                        .map(UserResponse::new)
                        .collect(Collectors.toList());
                sendResponse(resp, HttpServletResponse.SC_OK,
                        ServerResponse.success(userResponses, "Users retrieved successfully."));
            } else {
                int id = extractIdFromPath(pathInfo);
                User user = userService.getUserById(id);
                UserResponse userResponse = new UserResponse(user);
                sendResponse(resp, HttpServletResponse.SC_OK,
                        ServerResponse.success(userResponse, "User retrieved successfully."));
            }
        } catch (ApplicationException e) {
            sendErrorResponse(resp, e.getStatusCode(), e.getMessage());
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CreateUserRequest createUserRequest = objectMapper.readValue(req.getReader(), CreateUserRequest.class);
            userService.createUser(createUserRequest);
            sendResponse(resp, HttpServletResponse.SC_CREATED,
                    ServerResponse.success(null, "User created successfully."));
        } catch (ApplicationException e) {
            sendErrorResponse(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = extractIdFromPath(req.getPathInfo());
            UpdateUserRequest updateUserRequest = objectMapper.readValue(req.getReader(), UpdateUserRequest.class);
            userService.updateUser(id, updateUserRequest);
            sendResponse(resp, HttpServletResponse.SC_OK,
                    ServerResponse.success(null, "User updated successfully."));
        } catch (ApplicationException e) {
            sendErrorResponse(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = extractIdFromPath(req.getPathInfo());
            userService.deleteUser(id);
            sendResponse(resp, HttpServletResponse.SC_OK,
                    ServerResponse.success(null, "User deleted successfully."));
        } catch (ApplicationException e) {
            sendErrorResponse(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.");
        }
    }

    private int extractIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ApplicationException(HttpServletResponse.SC_BAD_REQUEST,
                    "User ID is missing in the URL path.");
        }
        try {
            String idStr = pathInfo.substring(1);
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new ApplicationException(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid User ID format in URL path: " + pathInfo);
        }
    }

    private void sendResponse(HttpServletResponse resp, int statusCode, ServerResponse response)
            throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print(objectMapper.writeValueAsString(response));
        }
    }

    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message)
            throws IOException {
        sendResponse(resp, statusCode, ServerResponse.error(message, statusCode));
    }
}