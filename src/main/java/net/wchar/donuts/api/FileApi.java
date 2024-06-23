package net.wchar.donuts.api;

import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.sys.util.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

/**
 * 文件上传
 * @author Elijah
 */
@Tag(name = "文件上传", description = "文件上传")
@RestController
@RequestMapping("/file")
public class FileApi implements InitializingBean {
    //文件访问前缀
    public static final String FILE_PREFIX  = "/file";

    //默认头像路径
    public static final String DEFAULT_AVATAR  = "/img/avatar.jpg";


    @Value("${file.upload.dir}")
    private String fileUploadDir;

    private Path fileStorageLocation;

    @Operation(summary = "上传文件")
    @Parameters({
            @Parameter(name = "file", description = "文件", required = true),
    })
    @PostMapping("/upload.action")
    public ResultDomain<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = storeFile(file);
        String filePath = getFilePath(fileName);
        return ResultUtils.success(filePath);
    }

    @GetMapping("/{year}/{month}/{day}/{fileName:.+}")
    @Operation(summary = "访问文件")
    public ResponseEntity<Resource> downloadFile(@PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String fileName, HttpServletRequest request) {
        try {
            Path filePath = fileStorageLocation.resolve(Paths.get(year, month, day, fileName)).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                if (contentType.startsWith("image/")) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .body(resource);
                } else {
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getFilePath(String fileName) {
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String month = String.format("%02d", today.getMonthValue());
        String day = String.format("%02d", today.getDayOfMonth());
        return year + "/" + month + "/" + day + "/" + fileName;
    }
    private String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            if (originalFileName.contains("..")) {
                throw new BusException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex >= 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }
            String newFileName = UUID.randomUUID() + fileExtension;
            LocalDate today = LocalDate.now();
            String year = String.valueOf(today.getYear());
            String month = String.format("%02d", today.getMonthValue());
            String day = String.format("%02d", today.getDayOfMonth());
            Path targetLocation = this.fileStorageLocation.resolve(Paths.get(year, month, day, newFileName));
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException ex) {
            throw new BusException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.fileStorageLocation = Paths.get(fileUploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BusException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}
