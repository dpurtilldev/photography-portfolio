import axios from 'axios';

const api = axios.create({ baseURL: 'http://localhost:8181/api'});

export interface CameraSettings {
    cameraModel?: string;
    lens?: string;
    aperture?: string;
    shutterSpeed?: string;
    iso?: number;
    focalLength?: string;
}

export interface Photo {
    id: string;
    title: string;
    description?: string;
    fileName: string;
    blobUrl: string;  // Changed from filePath to blobUrl - this is the Azure Blob Storage URL
    fileType?: string;
    fileSize?: number;
    captureDate: string;
    uploadDate?: string;
    lastEditedDate?: string;
    cameraSettings?: CameraSettings;
    location?: string;
    tags?: string[];
    editingSoftware?: string;
    edited: boolean;  // Changed from isEdited to edited to match backend
    thumbnailPath?: string;
    public: boolean;  // Changed from isPublic to public to match backend
}

export async function fetchPhotos(): Promise<Photo[]>{
    const {data} = await api.get<Photo[]>('/photos');
    return data;
}