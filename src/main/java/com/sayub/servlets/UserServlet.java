package com.sayub.servlets;

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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = Logger.getLogger(UserServlet.class);

    @Override
    public void init() throws ServletException {
        log.info("UserServlet init");
        UserRepository userRepository = new UserRepositoryImpl();
        this.userService = new UserServiceImpl(userRepository);
    }

    public void handleResponse(HttpServletResponse response, Runnable task) {
        try {
            task.run();
        } catch (ApplicationException e) {
            log.error("ApplicationException: ", e);
            sendErrorResponse(response, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Exception: ", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) {
        handleResponse(response, () -> {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<UserResponse> userResponses = userService.getAllUsers().stream()
                        .map(UserResponse::new)
                        .collect(Collectors.toList());
                sendResponse(response, HttpServletResponse.SC_OK,
                        ServerResponse.success(userResponses, "Users retrieved successfully."));
            } else {
                int id = extractIdFromPath(pathInfo);
                User user = userService.getUserById(id);
                UserResponse userResponse = new UserResponse(user);
                sendResponse(response, HttpServletResponse.SC_OK,
                        ServerResponse.success(userResponse, "User retrieved successfully."));
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleResponse(resp, () -> {
            CreateUserRequest createUserRequest;
            try {
                createUserRequest = objectMapper.readValue(req.getReader(), CreateUserRequest.class);
            } catch (IOException e) {
                throw new ApplicationException(403, "Invalid request data");
            }
            userService.createUser(createUserRequest);
            sendResponse(resp, HttpServletResponse.SC_CREATED,
                    ServerResponse.success(null, "User created successfully."));
        });
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleResponse(resp, () -> {
            int id = extractIdFromPath(req.getPathInfo());
            UpdateUserRequest updateUserRequest;
            try {
                updateUserRequest = objectMapper.readValue(req.getReader(), UpdateUserRequest.class);
            } catch (IOException e) {
                throw new ApplicationException(403, "Invalid request data");
            }
            userService.updateUser(id, updateUserRequest);
            sendResponse(resp, HttpServletResponse.SC_OK,
                    ServerResponse.success(null, "User updated successfully."));
        });
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleResponse(resp, () -> {
            int id = extractIdFromPath(req.getPathInfo());
            userService.deleteUser(id);
            sendResponse(resp, HttpServletResponse.SC_OK,
                    ServerResponse.success(null, "User deleted successfully."));
        });
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

    private void sendResponse(HttpServletResponse resp, int statusCode, ServerResponse response) {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            log.error("Exception while writing response: ", e);
        }
    }

    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) {
        sendResponse(resp, statusCode, ServerResponse.error(message, statusCode));
    }
}
